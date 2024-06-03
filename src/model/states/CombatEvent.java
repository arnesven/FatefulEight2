package model.states;

import model.actions.AbilityCombatAction;
import model.actions.QuickCastPassiveCombatAction;
import model.actions.SneakAttackCombatAction;
import model.actions.CombatAction;
import model.combat.loot.CombatLoot;
import model.combat.Combatant;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import sprites.CombatSpeechBubble;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.sprites.AnimationManager;
import view.sprites.DamageValueEffect;
import view.sprites.RunOnceAnimationSprite;
import view.subviews.*;

import java.util.*;

public class CombatEvent extends DailyEventState {

    private final CombatStatistics combatStats;
    private final List<GameCharacter> participants;
    private final List<Enemy> enemies = new ArrayList<>();
    private List<Combatant> initiativeOrder;
    private int currentInit = 0;
    private final CombatSubView subView;
    private final CombatMatrix combatMatrix;
    private Combatant currentCombatant;
    private boolean selectingFormation;
    private final List<GameCharacter> backMovers = new ArrayList<>();
    private boolean partyFled = false;
    private boolean fleeingEnabled;
    private boolean blockCombat = false;
    private CombatAction selectedCombatAction = null;
    private Combatant selectedTarget;
    private final List<GameCharacter> allies = new ArrayList<>();
    private boolean isAmbush;
    private int timeLimit = Integer.MAX_VALUE;
    private int roundCounter = 1;
    private final List<MyPair<GameCharacter, SneakAttackCombatAction>> sneakAttackers;
    private final Set<GameCharacter> blockSneakAttack = new HashSet<>();
    private final List<CombatLoot> extraLoot = new ArrayList<>();
    private final List<Combatant> delayedCombatants = new ArrayList<>();
    private MyPair<GameCharacter, Integer> flameWall = null;
    private boolean inQuickCast = false;

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
        setInitiativeOrder();
        this.subView = new CombatSubView(this, combatMatrix, theme);
        this.fleeingEnabled = fleeingEnabled;
        this.isAmbush = isAmbush;
        this.sneakAttackers = new ArrayList<>();
        combatStats = new CombatStatistics();
    }

    public CombatEvent(Model model, List<Enemy> startingEnemies) {
        this(model, startingEnemies, new GrassCombatTheme(), true, false);
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.combatSong);
        StripedTransition.transition(model, subView);
        addAllies(new ArrayList<>(model.getParty().getTamedDragons().values()));
        if (allies.size() > 0) {
            model.getTutorial().allies(model);
        }
        if (isAmbush) {
            model.getTutorial().ambush(model);
        }
        setInitiativeOrder();
        AnimationManager.synchAnimations();
        model.setInCombat(true);
        setFormation(model);
        combatStats.startCombat(enemies, participants, allies);
        runQuickCastTurns(model);
        runCombatLoop(model);
        model.getLog().waitForAnimationToFinish();
        handleLootAndSummary(model);
        removeKilledPartyMembers(model, partyFled);
        removedKilledTamedDragons(model);
        removeCombatConditions(model);
        model.setGameOver(model.getParty().isWipedOut());
        model.playMainSong(); // TODO: Song is dependent on location...
        model.setInCombat(false);
    }

    private void runQuickCastTurns(Model model) {
        subView.displaySplashMessage("QUICK CAST");
        MyLists.forEach(
                MyLists.filter(
                        MyLists.transform(
                                MyLists.filter(initiativeOrder, (Combatant c) -> c instanceof GameCharacter &&
                                                                model.getParty().getPartyMembers().contains(c)),
                                (Combatant c) -> (GameCharacter)c),
                        (GameCharacter gc) -> AbilityCombatAction.getPassiveCombatActions(gc).contains(QuickCastPassiveCombatAction.getInstance())),
                (GameCharacter gc) -> handleCharacterTurn(model, gc, true));
    }

    private void setInitiativeOrder() {
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
        Collections.sort(initiativeOrder, (c1, c2) -> c2.getSpeed() - c1.getSpeed());
    }

    private void runCombatLoop(Model model) {
        while (true) {
            System.out.println("Combat Round " + roundCounter);
            doCombatRound(model);
            roundCounter++;
            currentCombatant = null;
            if (combatDone(model)) {
                break;
            }
            setInitiativeOrder();
            if (!checkForOverrun(model)) {
                setFormation(model);
                checkForOpportunityAttacks(model);
            }
            triggerConditions(model);
        }
    }

    private void handleLootAndSummary(Model model) {
        List<CombatLoot> combatLoot = null;
        if (isWipedOut()) {
            print("You have been wiped out! ");
        } else if (partyFled) {
            print("You have have fled battle. ");
        } else if (roundCounter > timeLimit) {
            print("Combat has been interrupted. ");
            subView.displaySplashMessage("INTERRUPT");
        } else {
            println("You are victorious in battle!");
            combatLoot = combatStats.generateCombatLoot(model);
            combatLoot.addAll(extraLoot);
            combatStats.calculateStatistics(roundCounter-1);
            StripedTransition.transition(model, new CombatSummarySubView(combatStats, combatLoot));
        }

        print("Press enter to continue.");
        waitForReturn();
        if (combatLoot != null) {
            model.getParty().giveCombatLoot(combatLoot);
        }
    }

    protected void removeCombatConditions(Model model) {
        MyLists.forEach(model.getParty().getPartyMembers(), Combatant::removeCombatConditions);
    }

    private void setFormation(Model model) {
        selectingFormation = true;
        backMovers.clear();
        combatMatrix.moveSelectedToParty();
        subView.displaySplashMessage("FORMATION");
        print("Use SPACE to toggle a character's formation. Press enter when you are done.");
        getModel().getTutorial().combatFormation(getModel());
        waitForReturn();
        selectingFormation = false;
    }

    private boolean frontRowIsOverrun(Model model) {
        return enemies.size() > (2 * (model.getParty().livingCharactersInFrontRow() + livingAllies()));
    }

    private void doCombatRound(Model model) {
        subView.displaySplashMessage("ROUND " + roundCounter);
        for ( ; currentInit < initiativeOrder.size() && !combatDone(model) ; currentInit++) {
            Combatant turnTaker = initiativeOrder.get(currentInit);
            if (turnTaker instanceof Enemy) {
                if (!isAmbush) {
                    handleEnemyTurn(model, turnTaker);
                }
            } else {
                if (hasFlameWall() && flameWall.first == turnTaker) {
                    removeFlameWall();
                }
                handleCharacterTurn(model, turnTaker, false);
            }
        }
        if (!combatDone(model)) {
            handleSneakAttacks(model);
        }
        isAmbush = false;
        delayedCombatants.clear();
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

    private void handleCharacterTurn(Model model, Combatant turnTaker, boolean isQuickCast) {
        inQuickCast = isQuickCast;
        currentCombatant = turnTaker;
        combatMatrix.moveSelectedToEnemy();
        GameCharacter character = (GameCharacter) turnTaker;
        if (!character.getsCombatTurn()) {
            if (!character.isDead()) {
                println(character.getName() + " turn was skipped.");
            }
        } else {
            print(character.getFirstName() + "'s " + (isQuickCast?"Quick Cast ":"") + "turn. ");
            if (isQuickCast) {
                model.getTutorial().quickCasting(model);
            } else {
                model.getTutorial().combatActions(model);
            }
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
        if (!isQuickCast) {
            character.decreaseTimedConditions(model, this);
        }
        inQuickCast = false;
    }

    private void handleSneakAttacks(Model model) {
        for (MyPair<GameCharacter, SneakAttackCombatAction> pair : sneakAttackers) {
            currentCombatant = pair.first;
            combatMatrix.moveSelectedToEnemy();
            pair.second.resolveSneakAttack(model, this);
            model.getLog().waitForAnimationToFinish();
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
            subView.displaySplashMessage("OVERRUN!");
            printAlert("Party overrun by enemies! All characters in back row are moved to front.");
            model.getLog().waitForAnimationToFinish();
            MyLists.forEach(back, (GameCharacter gc) -> toggleFormationFor(model, gc));
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

    public synchronized void addSpecialEffectsBetween(Combatant from, Combatant to, RunOnceAnimationSprite sprite) {
        subView.addSpecialEffectsBetween(from, to, sprite);
    }

    public void destroyEnemy(Model model, Enemy enemy, GameCharacter killer) {
        System.out.println("Destroying enemy " + enemy.getName());
        enemies.remove(enemy);
        combatMatrix.remove(enemy);
        if (killer != null) {
            System.out.println("Killer is " + killer.getName());
            combatStats.registerCharacterKill(killer, enemy);

        }
        enemy.doUponDeath(model, this, killer);
        for (GameCharacter gc : participants) {
            if (!gc.isDead()) {
                model.getParty().giveXP(model, gc, 5);
                if (gc == killer) {
                    model.getParty().giveXP(model, gc, combatStats.getKillsFor(killer)*5);
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
        combatStats.damageDealt(damage, damager);
        if (target.getHP() <= 0) {
            RunOnceAnimationSprite killAnimation = ((Enemy)target).getKillAnimation();
            if (killAnimation != null) {
                if (target.getDeathSound() != null) {
                    SoundEffects.playSound(target.getDeathSound());
                }
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
        combatStats.increaseFledEnemies();
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
        if (MyRandom.rollD10() + 5 < found.second.getSneakValue()) {
            System.out.println(randomTarget.getName() + " avoided attack by sneaking!");
            return true;
        }
        return false;
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

    private int delayIndex(Combatant performer) {
        int newIndex = initiativeOrder.size()-1;
        for (int i = initiativeOrder.size(); i > 0; --i) {
            if (!delayedCombatants.contains(initiativeOrder.get(i-1))) {
                return i;
            }
        }
        return newIndex;
    }

    public boolean canDelay(Combatant performer) {
        if (delayedCombatants.contains(performer)) {
            return false;
        }
        return delayIndex(performer) > initiativeOrder.indexOf(performer);
    }

    public void delayCombatant(Combatant performer) {
        initiativeOrder.remove(performer);
        int delayIndex = delayIndex(performer);
        initiativeOrder.add(delayIndex(performer), performer);
        delayedCombatants.add(performer);
        System.out.println("Delay index was " + delayIndex);
        currentInit--;
        if (performer instanceof GameCharacter) {
            println(((GameCharacter) performer).getFirstName() + " delayed " + hisOrHer(((GameCharacter) performer).getGender()) + " turn.");
        } else {
            println(performer.getName() + " delayed.");
        }
    }

    public CombatStatistics getStatistics() {
        return combatStats;
    }

    public void setFlameWall(GameCharacter performer, int masteryLevel) {
        this.flameWall = new MyPair<>(performer, masteryLevel);
    }

    public void checkFlameWallDamage(Model model, Combatant target) {
        if (hasFlameWall()) {
            int damage = flameWall.second;
            println(target.getName() + " takes " + damage + " damage from the fire wall!");
            if (target instanceof Enemy) {
                doDamageToEnemy(target, damage, flameWall.first);
            } else {
                target.addToHP(-1 * damage);
                checkForDead(model, (GameCharacter) target);
            }
            addFloatyDamage(target, damage, DamageValueEffect.MAGICAL_DAMAGE);
        }
    }

    public void checkForDead(Model model, GameCharacter target) {
        if (target.isDead()) {
            printAlert(target.getName() + " has been slain in combat!");
            if (model.getParty().getPartyMembers().contains(target)) {
                if (target.isLeader() && model.getParty().appointNewLeader()) {
                    println(model.getParty().getLeader().getFullName() + " is now the new leader of the party.");
                }
            }
        }
    }

    private void removeFlameWall() {
        flameWall = null;
    }

    public boolean hasFlameWall() {
        return flameWall != null;
    }

    public boolean isInQuickCast() {
        return inQuickCast;
    }

    private void removedKilledTamedDragons(Model model) {
        for (GameCharacter master : new ArrayList<>(model.getParty().getTamedDragons().keySet())) {
            if (model.getParty().getTamedDragons().get(master).isDead()) {
                model.getParty().getTamedDragons().remove(master);
            }
        }
    }
}
