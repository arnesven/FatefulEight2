package model.states;

import model.combat.CombatLoot;
import model.combat.Combatant;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.sprites.AnimationManager;
import view.subviews.*;

import java.util.*;

public class CombatEvent extends DailyEventState {

    private final List<Enemy> startingEnemies;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Combatant> initiativeOrder;
    private int currentInit = 0;
    private CombatSubView subView;
    private CombatMatrix combatMatrix;
    private Combatant currentCombatant;
    private Map<GameCharacter, List<Enemy>> destroyedEnemies;
    private boolean selectingFormation;
    private List<GameCharacter> backMovers = new ArrayList<>();
    private boolean partyFled = false;
    private boolean fleeingEnabled;

    public CombatEvent(Model model, List<Enemy> startingEnemies, CombatTheme theme, boolean fleeingEnabled) {
        super(model);
        selectingFormation = true;
        combatMatrix = new CombatMatrix();
        this.startingEnemies = startingEnemies;
        enemies.addAll(startingEnemies);
        combatMatrix.addEnemies(enemies);
        combatMatrix.addParty(model.getParty());
        setInitiativeOrder(model);
        destroyedEnemies = new HashMap<>();
        this.subView = new CombatSubView(this, combatMatrix, theme);
        this.fleeingEnabled = fleeingEnabled;
    }

    public CombatEvent(Model model, List<Enemy> startingEnemies) {
        this(model, startingEnemies, new GrassCombatTheme(), true);
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.combatSong);
        StripedTransition.transition(model, subView);
        AnimationManager.synchAnimations();
        model.setInCombat(true);
        setFormation(model);
        while (true) {
            doCombatRound(model);
            currentCombatant = null;
            if (combatDone(model)) {
                break;
            }
            setInitiativeOrder(model);
            if (!checkForOverrun(model)) {
                setFormation(model);
                checkForOpportunityAttacks(model);
            }
        }

        List<CombatLoot> combatLoot = null;
        if (model.getParty().isWipedOut()) {
            print("You have been wiped out! ");
        } else if (partyFled) {
            print("You have have fled battle. "); // TODO: Possible party members leave party
        } else {
            println("You are victorious in battle!");
            combatLoot = generateCombatLoot(model, destroyedEnemies);
            StripedTransition.transition(model, new CombatSummarySubView(model, sumUp(destroyedEnemies), 0, combatLoot));
        }

        println("Press enter to continue.");
        waitForReturn();
        if (combatLoot != null) {
            model.getParty().giveCombatLoot(combatLoot);
        }

        removeKilledPartyMembers(model, partyFled);

        model.setGameOver(model.getParty().isWipedOut());
        model.playMainSong(); // TODO: Song is dependent on location...
        model.setInCombat(false);
    }

    private List<CombatLoot> generateCombatLoot(Model model, Map<GameCharacter, List<Enemy>> destroyedEnemies) {
        System.out.println("Generating combat loot");
        List<CombatLoot> loot = new ArrayList<>();
        for (GameCharacter gc : destroyedEnemies.keySet()) {
            System.out.println(gc.getName() + " has killed " + destroyedEnemies.get(gc).size() + " enemies");
            for (Enemy e : destroyedEnemies.get(gc)) {
                CombatLoot l = e.getLoot(model);
                System.out.println("  The " + e.getName() + "'s loot is " + l.getText() +
                        ", " + l.getGold() + " gold, " + l.getRations() + " rations.");
                loot.add(l);
            }
        }
        return loot;
    }

    private int sumUp(Map<GameCharacter, List<Enemy>> destroyedEnemies) {
        int sum = 0;
        for (GameCharacter gc : destroyedEnemies.keySet()) {
            sum += destroyedEnemies.get(gc).size();
        }
        return sum;
    }

    private void setFormation(Model model) {
        selectingFormation = true;
        backMovers.clear();
        combatMatrix.moveSelectedToParty();
        print("Use SPACE to toggle a character's formation. Press enter when you are done.");
        getModel().getTutorial().combatFormation(getModel());
        waitForReturn();
        selectingFormation = false;
    }

    private boolean frontRowIsOverrun(Model model) {
        return enemies.size() > 2 * model.getParty().livingCharactersInFrontRow();
    }

    private void doCombatRound(Model model) {
        for ( ; currentInit < initiativeOrder.size() && !combatDone(model) ; currentInit++) {
            Combatant turnTaker = initiativeOrder.get(currentInit);
            if (turnTaker instanceof Enemy) {
                List<Enemy> enms = new ArrayList<>(enemies);
                for (Enemy e : enms) {
                    if (e.getEnemyGroup() == ((Enemy) turnTaker).getEnemyGroup()) {
                        currentCombatant = e;
                        model.getLog().waitForAnimationToFinish();
                        e.takeCombatTurn(model, this);
                    }
                }
            } else {
                currentCombatant = turnTaker;
                combatMatrix.moveSelectedToEnemy();
                getCurrentCombatant().takeCombatTurn(model, this);
            }
        }
    }

    private boolean checkForOverrun(Model model) {
        List<GameCharacter> back = new ArrayList<>();
        back.addAll(model.getParty().getBackRow());
        if (!back.isEmpty() && frontRowIsOverrun(model)) {
            println("Party overrun by enemies! All characters in back row are moved to front.");
            for (GameCharacter gc : back) {
                toggleFormationFor(model, gc);
            }
            return true;
        }
        return false;
    }

    private void checkForOpportunityAttacks(Model model) {
        for (GameCharacter backMover : backMovers) {
            if (frontRowIsOverrun(model)) {
                // TODO: use stamina to re-roll.
                SkillCheckResult result = backMover.testSkill(Skill.Acrobatics, 7);
                if (result.isSuccessful()) {
                    println(backMover.getFirstName() + " avoided opportunity attack while moving back (Acrobatics " + result.asString() + ").");
                } else {
                    println(backMover.getFirstName() + " took 1 damage from opportunity attack while moving to back row (Acrobatics " + result.asString() + ").");
                    backMover.addToHP(-1);
                    addStrikeEffect(backMover, model, 1, false);
                }
            }
        }
    }

    private void setInitiativeOrder(Model model) {
        currentInit = 0;
        initiativeOrder = new ArrayList<>();
        initiativeOrder.addAll(model.getParty().getPartyMembers());
        Map<Character, Enemy> groupMap = new HashMap<>();
        for (Enemy e : enemies) {
            if (!groupMap.containsKey(e.getEnemyGroup())) {
                groupMap.put(e.getEnemyGroup(), e);
            }
        }
        for (Character c : groupMap.keySet()) {
            initiativeOrder.add(groupMap.get(c));
        }
        Collections.shuffle(initiativeOrder);
        Collections.sort(initiativeOrder, new Comparator<Combatant>() {
            @Override
            public int compare(Combatant c1, Combatant c2) {
                return c2.getSpeed() - c1.getSpeed();
            }
        });
    }

    private boolean combatDone(Model model) {
        return allEnemiesDead() || model.getParty().isWipedOut() || partyFled;
    }

    private boolean allEnemiesDead() {
        for (Enemy en : enemies) {
            if (en.getHP() > 0) {
                return false;
            }
        }
        return true;
    }

    public List<Combatant> getInitiativeOrder() {
        return initiativeOrder;
    }

    public Combatant getCurrentCombatant() {
        return currentCombatant;
    }

    public Combatant getSelectedEnemy() {
        return combatMatrix.getSelectedElement();
    }

    public void addStrikeEffect(Combatant target, Model model, int damage, boolean critical) {
        subView.addStrikeEffect(target, model, damage, critical);
    }

    public void destroyEnemy(Model model, Enemy enemy, GameCharacter killer) {
        System.out.println("Destroying enemy " + enemy.getName());
        enemies.remove(enemy);
        combatMatrix.remove(enemy);
        if (killer != null) {
            System.out.println("Killer is " + killer.getName());
            if (!destroyedEnemies.containsKey(killer)) {
                destroyedEnemies.put(killer, new ArrayList<>());
            }
            destroyedEnemies.get(killer).add(enemy);
        }
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!gc.isDead()) {
                model.getParty().giveXP(model, gc, 5);
                if (gc == killer) {
                    model.getParty().giveXP(model, gc, destroyedEnemies.get(killer).size()*5);
                }
            }
        }
    }

    public boolean isSelectingFormation() {
        return selectingFormation;
    }

    public void toggleFormationFor(Model model, GameCharacter combatant) {
        if (!combatant.isDead()) {
            model.getParty().toggleFormationFor(combatant);
            combatMatrix.toggleFormationFor(combatant);
            if (model.getParty().getBackRow().contains(combatant)) {
                backMovers.add(combatant);
            }
        } else {
            println("");
            println("Cannot change formation for dead party member.");
        }
    }

    public void setPartyFled(boolean b) {
        partyFled = b;
    }

    public void doDamageToEnemy(Combatant target, int damage, GameCharacter damager) {
        target.addToHP(-damage);
        if (target.getHP() <= 0) {
            destroyEnemy(getModel(), (Enemy)target, damager);
        }
    }

    public boolean fleeingEnabled() {
        return fleeingEnabled;
    }

    public void setFleeingEnabled(boolean fleeingEnabled) {
        this.fleeingEnabled = fleeingEnabled;
    }

    public boolean fled() {
        return partyFled;
    }
}
