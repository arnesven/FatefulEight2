package model.states;

import model.Inventory;
import model.combat.CombatAction;
import model.combat.CombatLoot;
import model.combat.Combatant;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Condition;
import model.enemies.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.sprites.AnimationManager;
import view.sprites.RunOnceAnimationSprite;
import view.subviews.*;

import java.util.*;

public class CombatEvent extends DailyEventState {

    private final List<Enemy> startingEnemies;
    private List<GameCharacter> participants;
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
    private boolean blockCombat = false;
    private CombatAction selectedCombatAction = null;
    private Combatant selectedTarget;
    private List<GameCharacter> allies = new ArrayList<>();
    private boolean isAmbush;
    private int fledEnemies = 0;
    private int timeLimit = Integer.MAX_VALUE;
    private int roundCounter = 1;

    public CombatEvent(Model model, List<Enemy> startingEnemies, CombatTheme theme, boolean fleeingEnabled, boolean isAmbush) {
        super(model);
        selectingFormation = true;
        combatMatrix = new CombatMatrix();
        this.startingEnemies = startingEnemies;
        enemies.addAll(startingEnemies);
        combatMatrix.addEnemies(enemies);
        combatMatrix.addParty(model.getParty());
        this.participants = new ArrayList<>();
        this.participants.addAll(model.getParty().getFrontRow());
        this.participants.addAll(model.getParty().getBackRow());
        setInitiativeOrder(model);
        destroyedEnemies = new HashMap<>();
        this.subView = new CombatSubView(this, combatMatrix, theme);
        this.fleeingEnabled = fleeingEnabled;
        this.isAmbush = isAmbush;
    }

    public CombatEvent(Model model, List<Enemy> startingEnemies) {
        this(model, startingEnemies, new GrassCombatTheme(), true, false);
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.combatSong);
        StripedTransition.transition(model, subView);
        if (allies.size() > 0) {
            model.getTutorial().allies(model);
        }
        setInitiativeOrder(model);
        AnimationManager.synchAnimations();
        model.setInCombat(true);
        setFormation(model);
        while (true) {
            System.out.println("Combat Round " + roundCounter);
            doCombatRound(model);
            roundCounter++;
            currentCombatant = null;
            if (combatDone(model)) {
                break;
            }
            setInitiativeOrder(model);
            if (!checkForOverrun(model)) {
                setFormation(model);
                checkForOpportunityAttacks(model);
            }
            triggerConditions(model);
        }
        model.getLog().waitForAnimationToFinish();

        List<CombatLoot> combatLoot = null;
        if (isWipedOut()) {
            print("You have been wiped out! ");
        } else if (partyFled) {
            print("You have have fled battle. "); // TODO: Possible party members leave party
        } else if (roundCounter > timeLimit) {
            print("Combat has been interrupted. ");
        } else {
            println("You are victorious in battle!");
            combatLoot = generateCombatLoot(model, destroyedEnemies);
            StripedTransition.transition(model, new CombatSummarySubView(model, sumUp(destroyedEnemies), fledEnemies, combatLoot));
        }

        println("Press enter to continue.");
        waitForReturn();
        if (combatLoot != null) {
            model.getParty().giveCombatLoot(combatLoot);
        }

        removeKilledPartyMembers(model, partyFled);
        removeCombatConditions(model);

        model.setGameOver(model.getParty().isWipedOut());
        model.playMainSong(); // TODO: Song is dependent on location...
        model.setInCombat(false);
    }

    private void removeCombatConditions(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.removeCombatConditions();
        }
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
        return enemies.size() > (2 * (model.getParty().livingCharactersInFrontRow() + livingAllies()));
    }

    private void doCombatRound(Model model) {
        for ( ; currentInit < initiativeOrder.size() && !combatDone(model) ; currentInit++) {
            Combatant turnTaker = initiativeOrder.get(currentInit);
            if (turnTaker instanceof Enemy) {
                if (!isAmbush) {
                    List<Enemy> enms = new ArrayList<>(enemies);
                    for (Enemy e : enms) {
                        if (e.getEnemyGroup() == ((Enemy) turnTaker).getEnemyGroup()) {
                            currentCombatant = e;
                            model.getLog().waitForAnimationToFinish();
                            e.takeCombatTurn(model, this);
                        }
                    }
                }
            } else {
                currentCombatant = turnTaker;
                combatMatrix.moveSelectedToEnemy();
                GameCharacter character = (GameCharacter) turnTaker;
                if (!character.getsCombatTurn()) {
                    if (!character.isDead()) {
                        println(character.getName() + " turn was skipped.");
                    }
                } else {
                    print(character.getFirstName() + "'s turn. ");
                    model.getTutorial().combatActions(model);
                    waitToProceed();
                    selectedCombatAction.doAction(model, this, character, selectedTarget);
                    selectedCombatAction = null;
                    selectedTarget = null;
                }
                character.decreaseTimedConditions(model, this);
            }
        }
        isAmbush = false;
    }

    private void waitToProceed() {
        blockCombat = true;
        while (blockCombat) {
            sleep();
        }
    }

    private boolean checkForOverrun(Model model) {
        List<GameCharacter> back = new ArrayList<>();
        back.addAll(model.getParty().getBackRow());
        if (!back.isEmpty() && frontRowIsOverrun(model)) {
            println("Party overrun by enemies! All characters in back row are moved to front.");
            model.getLog().waitForAnimationToFinish();
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
                SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, backMover, Skill.Acrobatics, 7, 0, 0);
                if (result.isSuccessful()) {
                    println(backMover.getFirstName() + " avoided opportunity attack while moving back.");
                } else {
                    println(backMover.getFirstName() + " took 1 damage from opportunity attack while moving to back row.");
                    backMover.addToHP(-1);
                    addStrikeEffect(backMover, 1, false);
                }
            }
        }
    }

    private void setInitiativeOrder(Model model) {
        currentInit = 0;
        initiativeOrder = new ArrayList<>();
        initiativeOrder.addAll(participants);
        initiativeOrder.addAll(allies);
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
        return allEnemiesDead() || isWipedOut() || partyFled || roundCounter > timeLimit;
    }

    private boolean isWipedOut() {
        for (GameCharacter chara : participants) {
            if (chara.getHP() > 0) {
                return false;
            }
        }
        return true;
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

    public void addStrikeEffect(Combatant target, int damage, boolean critical) {
        subView.addStrikeEffect(target, damage, critical);
    }

    public void addSpecialEffect(Combatant target, RunOnceAnimationSprite sprite) {
        subView.addSpecialEffect(target, sprite);
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
        enemy.doUponDeath(model, this, killer);
        for (GameCharacter gc : participants) {
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
        target.takeCombatDamage(this, damage);
        if (target.getHP() <= 0) {
            RunOnceAnimationSprite killAnimation = ((Enemy)target).getKillAnimation();
            if (killAnimation != null) {
                subView.addSpecialEffect(target, killAnimation);
            }
            destroyEnemy(getModel(), (Enemy)target, damager);
            if (MyRandom.rollD10() > 5) {
                getModel().getParty().partyMemberSay(getModel(), damager,
                        List.of("Vanquished!", "Destroyed!", "Don't mess with me.",
                                "That one won't be bothering us any more.",
                                "One less to worry about.", "Huzzah!",
                                "Another one bites the dust.", "Go back whence you come!",
                                "I'm on a roll!", "Bye bye!", "Don't come back!",
                                "Slain."));
            }
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

    public void unblock(CombatAction selectedAction, Combatant target) {
        blockCombat = false;
        this.selectedCombatAction = selectedAction;
        this.selectedTarget = target;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean playerHasSelectedAction() {
        return selectedCombatAction != null;
    }


    public void addAllies(List<GameCharacter> gcs) {
        this.allies.addAll(gcs);
        combatMatrix.addAllies(gcs);
    }

    public Collection<? extends GameCharacter> getAllies() {
        return allies;
    }

    private int livingAllies() {
        int sum = 0;
        for (GameCharacter gc : allies) {
            if (!gc.isDead()) {
                sum++;
            }
        }
        return sum;
    }

    public boolean isAmbush() {
        return isAmbush;
    }

    public void retreatEnemy(Combatant target) {
        enemies.remove(target);
        combatMatrix.remove(target);
        fledEnemies++;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean didTimeOut() {
        return timeLimit > roundCounter;
    }

    private void triggerConditions(Model model) {
        List<Combatant> combs = new ArrayList<>();
        combs.addAll(combatMatrix.getElementList());
        for (Combatant comb : combs) {
            comb.conditionsEndOfCombatRoundTrigger(model, this);
        }
    }

    private static boolean hasRangedEnemy(List<Enemy> enemies) {
        for (Enemy e : enemies) {
            if (e.isRanged()) {
                return true;
            }
        }
        return false;
    }
}
