package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillChecks;
import model.combat.conditions.RoutedCondition;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.items.Item;
import model.items.potions.CommonPoison;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.tasks.AssassinationDestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.combat.CombatTheme;
import view.combat.MansionTheme;
import view.combat.TownCombatTheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AssassinationEndingEvent extends DailyEventState {

    private static final int DOOR_LOCK_DIFFICULTY = 7;
    private final boolean comesOutWhenKnock;
    private final int comesOutOfHouseChance;

    public enum Ending { success, failure, unresolvedContinue, unresolvedEndOfDay};

    public AssassinationEndingEvent(Model model, boolean comesOutWhenKnock, int comesOutOfHouseChance) {
        super(model);
        this.comesOutWhenKnock = comesOutWhenKnock;
        this.comesOutOfHouseChance = comesOutOfHouseChance;
    }

    @Override
    protected void doEvent(Model model) {
        throw new IllegalStateException("Should not call doEvent on AssassinationEndingEvent.");
    }

    public Ending enterHome(Model model, AssassinationDestinationTask task) {
        String place = task.getWrit().getDestinationShortDescription();
        println("You walk up to the front door of the " + place + " and feel the doorknob.");
        if (MyRandom.flipCoin()) {
            println("The door is locked.");
            boolean success = model.getParty().doSoloLockpickCheck(model, this, DOOR_LOCK_DIFFICULTY);
            if (!success) {
                print("The door remains firmly locked. Do you want to knock on the door? (Y/N) ");
                if (yesNoInput()) {
                    return knockedOnDoor(model, task, place);
                }
                print("Do you want to break down the door? (Y/N) ");
                if (yesNoInput()) {
                    int damage = SkillChecks.doDamageToDoor(model, this);
                    if (damage > 2) {
                        println("The door knob comes right off and the door swings open. You quickly step inside.");
                        return enterHomePartTwo(model, task, place);
                    }
                    return knockedOnDoor(model, task, place);
                }
                return getSpottedByWoman(model, task);

            }
        } else {
            println("The door is unlocked, so you open it and step inside.");
        }
        return enterHomePartTwo(model, task, place);
    }

    protected Ending getSpottedByWoman(Model model, AssassinationDestinationTask task) {
        leaderSay("Hmm, maybe we should try something else?");
        println("As you're standing there pondering your options, you spot a woman in a window in a house nearby. The woman " +
                "seems to be watching you with some suspicion.");
        leaderSay("Maybe we should just get out of here for now. We'll come back later and deal with " +
                task.getWrit().getName() + ".");
        return Ending.unresolvedEndOfDay;
    }

    protected Ending knockedOnDoor(Model model, AssassinationDestinationTask task, String place) {
        if (!comesOutWhenKnock) {
            println("You wait for some time but nobody comes to the door.");
            return getSpottedByWoman(model, task);
        }
        String raceName = getRaceName(task);
        println("The door opens from the inside. A " + manOrWoman(task.getWrit().getGender()) +
                " peeks out, a " + raceName + ".");
        model.getLog().waitForAnimationToFinish();
        leaderSay("Are you " + task.getWrit().getName() + "?");
        GameCharacter victim = task.getWrit().makeVictim();
        showExplicitPortrait(model, victim.getAppearance(), task.getWrit().getName());
        portraitSay("Uhm, yes. And you are?");
        leaderSay("Your executioner!");
        println("You push into the " + place + " and attack the " + raceName + ".");
        victimReactToAttack(task.getWrit().getName());
        Ending result = runCombatWithVictimInside(model, task, victim, false);
        println("You leave the " + place + ".");
        return result;
    }

    protected void victimReactToAttack(String name) {
        printQuote(name, "Help! I'm being attacked!");
    }

    protected Ending enterHomePartTwo(Model model, AssassinationDestinationTask task, String place) {
        String raceName = getRaceName(task);
        println("Inside the " + place + " you find a " + manOrWoman(task.getWrit().getGender()) +
                ", a " + raceName + ".");
        model.getLog().waitForAnimationToFinish();
        leaderSay("Are you " + task.getWrit().getName() + "?");
        GameCharacter victim = task.getWrit().makeVictim();
        showExplicitPortrait(model, victim.getAppearance(), task.getWrit().getName());
        portraitSay("Uhm, yes. And you are?");
        leaderSay("Your executioner!");
        victimReactToAttack(task.getWrit().getName());
        Ending result = runCombatWithVictimInside(model, task, victim, false);
        println("You leave the " + place + ".");
        return result;
    }

    protected Ending runCombatWithVictim(Model model, AssassinationDestinationTask task, GameCharacter victim,
                                         boolean surprise, CombatTheme theme, int victimFleeChance,
                                         boolean addNotoriety) {
        FormerPartyMemberEnemy enemy = new FormerPartyMemberEnemy(victim);
        List<Enemy> companions = getVictimCompanions(model, task, addNotoriety);
        addConditionsToVictimEnemy(model, task, enemy, companions, victimFleeChance, addNotoriety);
        List<Enemy> enemies = new ArrayList<>(List.of(enemy));
        enemies.addAll(companions);
        Collections.shuffle(enemies);
        if (surprise) {
            runSurpriseCombat(enemies, theme, true);
        } else {
            runCombat(enemies, theme, true);
        }
        setCurrentTerrainSubview(model);
        Ending ending;
        if (!haveFledCombat()) {
            if (enemy.isDead()) {
                if (addNotoriety) {
                    GeneralInteractionEvent.addMurdersToNotoriety(model, this, 1);
                }
                println(model.getParty().getLeader().getName() + " looks down at " + task.getWrit().getName() + "'s corpse.");
                leaderSay("Rest in peace " + task.getWrit().getName() + ".");
                ending = Ending.success;
            } else {
                if (addNotoriety) {
                    GeneralInteractionEvent.addAssaultsToNotoriety(model, this, 1);
                }
                leaderSay("Darn it, " + heOrShe(task.getWrit().getGender()) + " got away! Just " + myOrOur() + " luck!");
                ending = Ending.failure;
            }
        } else {
            if (addNotoriety) {
                GeneralInteractionEvent.addAssaultsToNotoriety(model, this, 1);
            }
            ending = Ending.failure;
        }
        return ending;
    }

    protected void addConditionsToVictimEnemy(Model model, AssassinationDestinationTask task,
                                              FormerPartyMemberEnemy enemy, List<Enemy> companions,
                                              int victimFleeChance, boolean isOutside) {
        enemy.addCondition(new RoutedCondition(victimFleeChance));
    }

    protected List<Enemy> getVictimCompanions(Model model, AssassinationDestinationTask task, boolean isOutside) {
        return new ArrayList<>();
    }

    protected Ending runCombatWithVictimInside(Model model, AssassinationDestinationTask task, GameCharacter victim, boolean surprise) {
        return runCombatWithVictim(model, task, victim, surprise, new MansionTheme(), 5, false);
    }

    protected Ending runCombatWithVictimOutside(Model model, AssassinationDestinationTask task, GameCharacter victim, boolean surprise) {
        return runCombatWithVictim(model, task, victim, surprise, new TownCombatTheme(), RoutedCondition.DEFAULT_ESCAPE_CHANCE, true);
    }

     public Ending layInWait(Model model, AssassinationDestinationTask task) {
        String placeName = task.getWrit().getDestinationShortDescription();
        String raceName = getRaceName(task);
        leaderSay("Let's just hide nearby and wait for this " + raceName + " to come out of " +
                hisOrHer(task.getWrit().getGender()) + " home.");
        boolean said = randomSayIfPersonality(PersonalityTrait.critical,
                List.of(model.getParty().getLeader()), "And what if...");
        if (said) {
            leaderSay(heOrSheCap(task.getWrit().getGender()) + " must come out sometime.");
        }
        if (MyRandom.rollD10() <= comesOutOfHouseChance) { // comesOutOfHouseChance
            println("A little while later a " + manOrWoman(task.getWrit().getGender()) + " comes out " +
                    "of the " + placeName + ", it's a " + raceName + ".");

            Ending end = attemptRangedSneakAttack(model, task);
            if (end != Ending.unresolvedContinue) {
                return end;
            }
            println("You quickly step out into the street to intercept the " + raceName + ".");
            leaderSay("Are you " +  task.getWrit().getName() + "?");
            GameCharacter victim = task.getWrit().makeVictim();
            showExplicitPortrait(model, victim.getAppearance(), task.getWrit().getName());
            portraitSay("Uhm, yes. And you are?");
            leaderSay("Your executioner!");
            return runCombatWithVictimOutside(model, task, victim, false);
        }
        println("Several hours pass, but nobody emerges from the " + placeName + ".");
        randomSayIfPersonality(PersonalityTrait.critical,
                List.of(model.getParty().getLeader()), "I don't want to say I told you so, but...");
        return getSpottedByWoman(model, task);
    }

    private Ending attemptRangedSneakAttack(Model model, AssassinationDestinationTask task) {
        String raceName = getRaceName(task);
        if (MyLists.any(model.getParty().getPartyMembers(), gc -> gc.getEquipment().getWeapon().isRangedAttack())) {
            print("At least one of your party members has a ranged weapon. " +
                    "Would you like to make a ranged sneak attack on the " + raceName + "? (Y/N) ");
            if (!yesNoInput()) {
                return Ending.unresolvedContinue;
            }
        }

        GameCharacter shooter = model.getParty().getPartyMember(0);
        while (model.getParty().size() > 1) {
            println("Who should perform the ranged attack?");
            shooter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (!shooter.getEquipment().getWeapon().isRangedAttack()) {
                println(shooter.getFirstName() + " does not have a ranged weapon.");
            } else {
                break;
            }
        }

        GameCharacter victim = task.getWrit().makeVictim();
        FormerPartyMemberEnemy dummyEnemy = new FormerPartyMemberEnemy(victim);
        CombatEvent combat = new CombatEvent(model, List.of(dummyEnemy));
        shooter.doOneAttack(model, combat, dummyEnemy, true, 0,
                shooter.getEquipment().getWeapon().getCriticalTarget()-1);

        if (dummyEnemy.isDead()) {
            println("The target is instantly killed by the projectile, the body falling numbly to the ground.");
            leaderSay("That was a nice shot. Now let's get out of here.");
            return Ending.success;
        }
        if (dummyEnemy.getHP() < dummyEnemy.getMaxHP()) {
            println("The " + raceName + " is startled by the sound of the projectile whizzing by. " +
                    "Confused " + heOrShe(task.getWrit().getGender()) + " looks around, then hurries off down the street.");
        }
        println("The " + raceName + " stumbles to " + hisOrHer(task.getWrit().getGender()) + " knees, and exclaims loudly.");
        printQuote(raceName, "Ouch! Help! Somebody is attacking me!");
        leaderSay("Darn it. I guess we'll just have to finish the job the old fashioned way.");
        victim.addToHP(-(victim.getMaxHP() - dummyEnemy.getHP()));
        println("You rush up to the " + raceName + " with weapons drawn!");
        return runCombatWithVictimOutside(model, task, victim, false);
    }

    public Ending sneakIntoHome(Model model, AssassinationDestinationTask task) {
        String place = task.getWrit().getDestinationShortDescription();
        leaderSay("There's got to be a back door to this place. Or perhaps a window " + iOrWe() +
                " can climb through?");
        print("You sneak around the back of the " + place + " and there is indeed a");
        if (MyRandom.flipCoin()) {
            println(" backdoor. You feel the doorknob. The door is unlocked and you proceed inside.");
        } else {
            println("n open window to climb through.");
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 3);
            if (!failers.isEmpty()) {
                print("Do you wish to proceed inside the " + place + " without " +
                        MyLists.commaAndJoin(failers, GameCharacter::getFirstName) + "? (Y/N)");
                if (!yesNoInput()) {
                    return getSpottedByWoman(model, task);
                }
                model.getParty().benchPartyMembers(failers);
            }
        }
        return sneakAroundInside(model, task, place);
    }

    protected Ending sneakAroundInside(Model model, AssassinationDestinationTask task, String place) {
        boolean success = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 5);
        String raceName = getRaceName(task);
        GameCharacter victim = task.getWrit().makeVictim();
        Ending result;
        if (!success) {
            printQuote("Somebody", "Hello? Is somebody there?");
            println("A " + raceName + " appears.");
            model.getLog().waitForAnimationToFinish();
            leaderSay("Are you " + task.getWrit().getName() + "?");
            showExplicitPortrait(model, victim.getAppearance(), task.getWrit().getName());
            portraitSay("Yes I am, but who are you, and what are you doing in my home?");
            leaderSay("Don't worry, " + iOrWe() + " won't be here long. " + imOrWere() + " just here to kill you!");
            victimReactToAttack(task.getWrit().getName());
            result = runCombatWithVictimInside(model, task, victim, false);
        } else {
            println("You spot a " + raceName + " in the " + place + ". " + heOrSheCap(task.getWrit().getGender()) +
                    " seems completely unaware as you approach with your weapons drawn.");
            result = runCombatWithVictimInside(model, task, victim, true);
        }
        println("You leave the " + place + ".");
        return result;
    }

    protected String getRaceName(AssassinationDestinationTask task) {
        return task.getWrit().getRace().getName().toLowerCase();
    }

    public Ending usePoison(Model model, AssassinationDestinationTask task, MyPair<String, String> objectAtDoor) {
        println("You apply the poison to the " + objectAtDoor.second + ".");
        leaderSay("Now " + iOrWe() + " wait, I guess.");
        println("You hide yourself in the bushes near the " + task.getWrit().getDestinationShortDescription() + ".");
        Item poison = MyLists.find(model.getParty().getInventory().getAllItems(),
                it -> it instanceof CommonPoison);
        model.getParty().getInventory().remove(poison);
        return usePoisonPartTwo(model, task, objectAtDoor);
    }

    protected Ending usePoisonPartTwo(Model model, AssassinationDestinationTask task,
                                      MyPair<String, String> objectAtDoor) {
        String place = task.getWrit().getDestinationShortDescription();
        String raceString = task.getWrit().getRace().getName().toLowerCase();
        println("You wait in the bushes for several hours. You're just about to give up when the door opens " +
                "and a " + raceString + " briefly steps outside to collect the " + objectAtDoor.second + ". " +
                heOrSheCap(task.getWrit().getGender()) + " then disappears back into the " +
                place + ".");
        if (model.getParty().size() > 1) {
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                    "What do we do now?");
        }
        leaderSay("Uhm... Let's just wait some more and see what happens.");
        int roll = MyRandom.rollD6();
        if (roll < 3) {
            println("A little while later a woman approaches the " + place + " and knocks on the door. " +
                    "When nobody answers she opens the door and steps into the house. Shortly thereafter you hear " +
                    "high-pitched screams.");
            leaderSay("Okay... That's " + myOrOur() + " cue. Let's go.");
        } else if (roll < 5) {
            println("About an hour later you suddenly hear strange guttural sounds coming from the " + place + ". " +
                    "Then, the door swings open and the " + raceString + " stumbles out, clutching " +
                    hisOrHer(task.getWrit().getGender()) + " gut. " + heOrSheCap(task.getWrit().getGender()) + " moans " +
                    "a few times, and then, with a loud thump " + heOrShe(task.getWrit().getGender()) +
                    " falls to the ground.");
            leaderSay("I think we got " + hisOrHer(task.getWrit().getGender()) + ". Let's go.");
        } else {
            println("An hour passes.");
            leaderSay("Maybe I should just sneak up and see if I can see something.");
            println("You creep up to the " + place + " and start looking through each window. Soon you spot the " +
                    raceString + " lying on the floor, immobile.");
            leaderSay(heOrSheCap(task.getWrit().getGender()) + "'s dead. Let's go.");
        }
        println("Having carried out your mission, you leave the " + place + ".");
        return Ending.success;
    }

    protected void findSpareChange(Model model) {
        int gold = MyRandom.rollD10() + 2;
        int obols = MyRandom.randInt(10, 150);
        println("You found " + gold + " gold and " + obols + " obols.");
        model.getParty().addToGold(gold);
        model.getParty().addToObols(obols);
    }

    public Ending leave(Model model, AssassinationDestinationTask task) {
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Now is not the time for this.", "Something about this doesn't feel right.",
                        "No. We're not doing this.", "Hmm. Not today.", "Come on. We're leaving."));
        println("You walk away from the " + task.getWrit().getDestinationShortDescription() + ".");
        return Ending.unresolvedContinue;
    }
}
