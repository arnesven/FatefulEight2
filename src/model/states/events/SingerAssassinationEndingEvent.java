package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.CowardlyCondition;
import model.enemies.BodyGuardEnemy;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.behaviors.KnockDownAttackBehavior;
import model.races.HumanRace;
import model.races.Race;
import model.tasks.AssassinationDestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class SingerAssassinationEndingEvent extends AssassinationEndingEvent {
    private AdvancedAppearance bodyguardAppearance;
    private boolean bodyguardRemoved = false;

    public SingerAssassinationEndingEvent(Model model) {
        super(model, true, 8);
    }

    @Override
    protected Ending knockedOnDoor(Model model, AssassinationDestinationTask task, String place) {
        String bodyguardRaceString = generateBodyguardRace(task).getName().toLowerCase();
        println("The door opens from the inside. A large " + bodyguardRaceString + " peeks out.");
        model.getLog().waitForAnimationToFinish();
        leaderSay("Are you " + task.getWrit().getName() + "?");
        showExplicitPortrait(model, bodyguardAppearance, MyStrings.capitalize(bodyguardRaceString));
        portraitSay("No. Go away!");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 9);
        if (!success) {
            leaderSay("But we're looking for " + task.getWrit().getName() +
                    ". Can we come in and look for " + himOrHer(task.getWrit().getGender()) +
                    " so we can, uh... Be nice to " + himOrHer(task.getWrit().getGender()) + "?");
            println("The " + bodyguardRaceString + " promptly shuts the door.");
            return Ending.failure;
        }
        leaderSay("But we're acquaintances of " + task.getWrit().getName() + ", and we have some " +
                "important news to bring " + himOrHer(task.getWrit().getGender()) + ".");
        portraitSay("Hmm. I suppose that's all right.");
        println("The " + bodyguardRaceString + " let's you into the " + place + ".");
        return enterHomePartTwo(model, task, place);
    }

    private Race generateBodyguardRace(AssassinationDestinationTask task) {
        if (bodyguardAppearance == null) {
            List<Race> races;
            if (task.getWrit().getRace() instanceof HumanRace) {
                races = List.of(Race.HALF_ORC, Race.DWARF);
            } else {
                races = new ArrayList<>(List.of(Race.HALF_ORC, Race.DWARF, Race.NORTHERN_HUMAN, Race.SOUTHERN_HUMAN));
                races.removeIf(r -> r.id() == task.getWrit().getRace().id());
            }
            this.bodyguardAppearance = PortraitSubView.makeRandomPortrait(Classes.BANDIT, MyRandom.sample(races));
        }
        return this.bodyguardAppearance.getRace();
    }

    @Override
    protected void victimReactToAttack(String name) {
        printQuote(name, "Are you sure you want to do this?");
    }

    @Override
    protected Ending runCombatWithVictimInside(Model model, AssassinationDestinationTask task, GameCharacter victim, boolean ambush) {
        showExplicitPortrait(model, victim.getAppearance(), victim.getName());
        leaderSay("What do you mean?");
        portraitSay("My bodyguard here is a pretty tough cookie. I hired " +
                himOrHer(task.getWrit().getGender()) + " with exactly this type of situation in mind.");
        print("Do you want to deal with the bodyguard (Y) or simply attack (N)? ");
        if (yesNoInput()) {
            this.bodyguardRemoved = dealWithBodyguard(model, task, victim);
            println("The bodyguard leaves the " + task.getWrit().getDestinationShortDescription() + ".");
            model.getLog().waitForAnimationToFinish();
            showExplicitPortrait(model, victim.getAppearance(), victim.getName());
            portraitSay("What? No! Don't leave me!");
            leaderSay("Tough luck. Will those be your final words then?");
            portraitSay("Help! Eeeeiiii!");
        }
        return super.runCombatWithVictimInside(model, task, victim, ambush);
    }

    private boolean dealWithBodyguard(Model model, AssassinationDestinationTask task, GameCharacter victim) {
        println("You turn to the bodyguard.");
        generateBodyguardRace(task);
        showExplicitPortrait(model, bodyguardAppearance, "Bodyguard");
        List<String> options = new ArrayList<>(List.of("Persuade", "Intimidate", "Bribe"));
        if (model.getParty().getGold() < 5) {
            options.remove(2);
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (choice == 0) {
            return persuadeBodyguard(model, victim);
        }
        if (choice == 1) {
            return intimidateBodyguard(model);
        }
        return bribeBodyguard(model, victim);
    }

    private boolean persuadeBodyguard(Model model, GameCharacter victim) {
        MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 10);
        if (result.first) {
            partyMemberSay(result.second, "Look. This will probably get pretty messy." +
                    " Why don't you just spare yourself that and leave now?");
            portraitSay("Fair enough. I never really liked this buffoon anyway.");
            return true;
        }
        partyMemberSay(result.second, "Can you just wait outside for a second? We're just going to rough " +
                victim.getFirstName() + " up a bit.");
        portraitSay("I'm not going anywhere.");
        return false;
    }

    private boolean intimidateBodyguard(Model model) {
        GameCharacter gc = model.getParty().getPartyMember(0);
        if (model.getParty().size() > 0) {
            print("Who should attempt to intimidate the bodyguard?");
            gc = model.getParty().partyMemberInput(model, this, gc);
        }
        SkillCheckResult result = model.getParty().doIntimidationSkillCheck(model, this, gc, 10, 0);
        if (result.isSuccessful()) {
            partyMemberSay(gc, "Look. We don't want to kill you, but if you get in our way, you're dead meat. You got that?");
            portraitSay("Message received, loud and clear. I'm outta here.");
            return true;
        }
        partyMemberSay(gc, imOrWereCap() + " like the strongest, toughest ever. Fight " + meOrUs() + ", and you'll be sorry!");
        portraitSay("Yeah, yeah. Super scary. Not.");
        return false;
    }

    private boolean bribeBodyguard(Model model, GameCharacter victim) {
        leaderSay("I don't know what " + victim.getName() + " is paying you, but here is some gold. Take it and leave.");
        print("How much would you like to bribe the bodyguard with?");
        List<Integer> golds = new ArrayList<>(List.of(1, 5, 20, 50, 100, 150));
        golds.removeIf(i -> model.getParty().getGold() < i);
        int choice = multipleOptionArrowMenu(model, 24, 22, MyLists.transform(golds, i -> i + " gold"));
        if (choice < 20) {
            portraitSay("You must be joking. Just get out of here fool.");
            return false;
        }
        if (choice > 100 || MyRandom.flipCoin()) {
            println("The bodyguard accepts the money.");
            portraitSay("Fair enough. I never really liked this buffoon anyway.");
            model.getParty().spendGold(golds.get(choice));
            return true;
        }
        portraitSay("Take your dirty money away from me. Now leave!");
        return false;
    }

    protected Ending usePoisonPartTwo(Model model, AssassinationDestinationTask task,
                                      MyPair<String, String> objectAtDoor) {
        String place = task.getWrit().getDestinationShortDescription();
        String bodyguardRaceString = generateBodyguardRace(task).getName().toLowerCase();
        String raceString = getRaceName(task);
        println("You wait in the bushes for several hours. You're just about to give up when the door opens " +
                "and a large " + bodyguardRaceString + " briefly steps outside to collect the " + objectAtDoor.second + ". " +
                heOrSheCap(task.getWrit().getGender()) + " then disappears back into the " +
                place + ".");

        if (model.getParty().size() > 1) {
            leaderSay("Yes! Got " + himOrHer(task.getWrit().getGender()) + "!");
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                    "Uhm. I don't think that was " + task.getWrit().getName() + ".");
            leaderSay("Why not?");
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                    "Wasn't the target a " + raceString + "? That looked like " + bodyguardRaceString + ".");
            leaderSay("Oh yeah. We may have just poisoned the wrong person.");
        } else {
            leaderSay("Uh-oh... that didn't look like a " + raceString + ". Did I just poison the wrong person?");
        }
        leaderSay("I guess " + iOrWe() + " can wait and see what happens.");
        println("About an hour later you suddenly hear strange guttural sounds coming from the " + place + ". " +
                "Then, the door swings open and the " + bodyguardRaceString + " stumbles out, clutching " +
                hisOrHer(task.getWrit().getGender()) + " gut. " + heOrSheCap(task.getWrit().getGender()) + " moans " +
                "a few times, and then, with a loud thump " + heOrShe(task.getWrit().getGender()) +
                " falls to the ground.");
        println("You walk over to have a look. A large muscular " + bodyguardRaceString +
                " clad in leather armor is lying face down in the street.");
        leaderSay("Hmm... yeah, that just can't be " + task.getWrit().getName() + ".");
        println("Just as you are about to help yourself to the " + bodyguardRaceString + "'s belongings you see a " +
                manOrWoman(task.getWrit().getGender()) + " in fancy clothing coming out of the house. Upon seeing you " +
                "and the fallen " + bodyguardRaceString + " " + heOrShe(task.getWrit().getGender()) +
                " starts sprinting down the street.");
        leaderSay("Ugh... That was probably our target right there. " +
                "I think it's safe to say we botched this assassination attempt.");
        println("You quickly loot the " + bodyguardRaceString + ", then hurry away from the crime scene.");
        int gold = MyRandom.rollD10();
        println("You find " + gold + " gold.");
        model.getParty().earnGold(gold);
        GeneralInteractionEvent.addMurdersToNotoriety(model, this, 1);
        return Ending.failure;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model, AssassinationDestinationTask task, boolean isOutside) {
        if (bodyguardRemoved) {
            return new ArrayList<>();
        }
        return List.of(new SingersBodyGuardEnemy('B'));
    }


    protected void addConditionsToVictimEnemy(Model model, AssassinationDestinationTask task,
                                              FormerPartyMemberEnemy enemy, List<Enemy> companions,
                                              int victimFleeChance, boolean isOutside) {
        if (bodyguardRemoved) {
            super.addConditionsToVictimEnemy(model, task, enemy, companions, victimFleeChance, isOutside);
        } else {
            enemy.addCondition(new CowardlyCondition(companions));
        }
    }

    private static class SingersBodyGuardEnemy extends BodyGuardEnemy {
        public SingersBodyGuardEnemy(char c) {
            super(c);
            setName("Bodyguard");
            setAttackBehavior(new KnockDownAttackBehavior(4));
        }

        @Override
        public int getDamageReduction() {
            return 1;
        }

        @Override
        public int getMaxHP() {
            return 13;
        }

        @Override
        public int getDamage() {
            return 5;
        }
    }
}
