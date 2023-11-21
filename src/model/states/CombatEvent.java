package model.states;

import model.actions.SneakAttackCombatAction;
import model.combat.CombatAction;
import model.combat.CombatLoot;
import model.combat.Combatant;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sprites.CombatSpeechBubble;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.AnimationManager;
import view.sprites.DamageValueEffect;
import view.sprites.RunOnceAnimationSprite;
import view.subviews.*;

import java.util.*;

public class CombatEvent extends DailyEventState {

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
    private final List<MyPair<GameCharacter, SneakAttackCombatAction>> sneakAttackers;
    private final Set<GameCharacter> blockSneakAttack = new HashSet<>();
    private List<CombatLoot> extraLoot = new ArrayList<>();

    public CombatEvent(Model model, List<Enemy> startingEnemies, CombatTheme theme, boolean fleeingEnabled, boolean isAmbush) {
        super(model);
        selectingFormation = true;
        combatMatrix = new CombatMatrix();
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
        this.sneakAttackers = new ArrayList<>();
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
        if (isAmbush) {
            model.getTutorial().ambush(model);
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

        print("Press enter to continue.");
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

    protected void removeCombatConditions(Model model) {
        MyLists.forEach(model.getParty().getPartyMembers(), Combatant::removeCombatConditions);
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
        loot.addAll(extraLoot);
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
                    handleEnemyTurn(model, turnTaker);
                }
            } else {
                handleCharacterTurn(model, turnTaker);
            }
        }
        if (!combatDone(model)) {
            handleSneakAttacks(model);
        }
        isAmbush = false;
    }

    private void handleEnemyTurn(Model model, Combatant turnTaker) {
        List<Enemy> enms = new ArrayList<>(enemies);
        for (Enemy e : enms) {
            if (e.getEnemyGroup() == ((Enemy) turnTaker).getEnemyGroup()) {
                currentCombatant = e;
                model.getLog().waitForAnimationToFinish();
                e.takeCombatTurn(model, this);
            }
        }
    }

    private void handleCharacterTurn(Model model, Combatant turnTaker) {
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
            do {
                waitToProceed();
                selectedCombatAction.executeCombatAction(model, this, character, selectedTarget);
                if (!selectedCombatAction.takeAnotherAction()) {
                    break;
                }
                selectedCombatAction = null;
            } while (true);
            selectedCombatAction = null;
            selectedTarget = null;
        }
        character.decreaseTimedConditions(model, this);
    }

    private void handleSneakAttacks(Model model) {
        for (MyPair<GameCharacter, SneakAttackCombatAction> pair : sneakAttackers) {
            currentCombatant = pair.first;
            combatMatrix.moveSelectedToEnemy();
            pair.second.resolveSneakAttack(model, this);
        }
        sneakAttackers.clear();
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
            printAlert("Party overrun by enemies! All characters in back row are moved to front.");
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
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, backMover, Skill.Acrobatics, 7, 0, 0);
            if (result.isSuccessful()) {
                println(backMover.getFirstName() + " avoided opportunity attack while moving back.");
            } else {
                println(backMover.getFirstName() + " took 1 damage from opportunity attack while moving to back row.");
                backMover.addToHP(-1);
                addFloatyDamage(backMover, 1, DamageValueEffect.STANDARD_DAMAGE);
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
        return MyLists.all(participants, (GameCharacter gc) -> gc.getHP() <= 0);
    }

    private boolean allEnemiesDead() {
        return MyLists.all(enemies, (Enemy en) -> en.getHP() <= 0);
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

    public void addFloatyDamage(Combatant target, int damage, MyColors color) {
        subView.addFloatyDamage(target, damage, color);
    }

    public void addFloatyText(Combatant target, int strikeTextEffect) {
        subView.addFloatyText(target, strikeTextEffect);
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
            if (MyRandom.rollD10() > 5 && getModel().getParty().getPartyMembers().contains(damager)) {
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
        MyLists.forEach(allies, (GameCharacter gc) -> combatMatrix.remove(gc));
        this.allies.addAll(gcs);
        combatMatrix.addAllies(allies);
    }

    public void removeAlly(GameCharacter gc) {
        this.allies.remove(gc);
        combatMatrix.remove(gc);
    }

    public List<GameCharacter> getAllies() {
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
        return roundCounter > timeLimit;
    }

    private void triggerConditions(Model model) {
        List<Combatant> combs = new ArrayList<>();
        combs.addAll(combatMatrix.getElementList());
        for (Combatant comb : combs) {
            comb.conditionsEndOfCombatRoundTrigger(model, this);
        }
    }

    public int getCurrentRound() {
        return roundCounter;
    }

    public void addSneakAttacker(GameCharacter chara, SneakAttackCombatAction action) {
        sneakAttackers.add(new MyPair<>(chara, action));
    }

    public void removeSneaker(GameCharacter randomTarget) {
        int size = sneakAttackers.size();
        sneakAttackers.removeIf((MyPair<GameCharacter, SneakAttackCombatAction> pair) -> pair.first == randomTarget);
        if (size > sneakAttackers.size()) {
            SneakAttackCombatAction.cancel(this, randomTarget);
        }
    }

    public boolean checkForSneakAvoidAttack(GameCharacter randomTarget) {
        MyPair<GameCharacter, SneakAttackCombatAction> found = null;
        for (MyPair<GameCharacter, SneakAttackCombatAction> sneaker : sneakAttackers) {
            if (sneaker.first == randomTarget) {
                found = sneaker;
            }
        }
        if (found == null) {
            return false;
        }
        return MyRandom.rollD10() + 5 < found.second.getSneakValue();
    }

    public boolean isEligibleForSneakAttack(GameCharacter performer) {
        return !blockSneakAttack.contains(performer);
    }

    public void blockSneakAttackFor(GameCharacter character) {
        blockSneakAttack.add(character);
    }

    public void addExtraLoot(List<CombatLoot> extraLoot) {
        this.extraLoot.addAll(extraLoot);
    }

    public void tookDamageTalk(GameCharacter gameCharacter, int damage) {
        if (damage > 0 && gameCharacter.getHP() < 3) {
            partyMemberSay(gameCharacter, MyRandom.sample(List.of("I'm dying!",
                    "I need healing!", "I don't feel so good...", "I need aid!")));
        } else if (MyRandom.randInt(5) < (damage-1)) {
            partyMemberSay(gameCharacter, MyRandom.sample(List.of("Ouch!", "That hurt!",
                    "The pain!", "Ugh, that's gonna leave a scar...", "I'm taking some damage here!",
                    "Get away from me!#", "Yeeouch!", "Argh!#", "Right in the...", "Ouchy!",
                    "Ugh!#", "That was painful!", "I'm hit!")));
            addSpecialEffect(gameCharacter, new CombatSpeechBubble());
        }
    }
}
