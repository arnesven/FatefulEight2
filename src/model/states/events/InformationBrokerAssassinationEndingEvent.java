package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.ChildAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.races.Race;
import model.states.dailyaction.LodgingState;
import model.tasks.AssassinationDestinationTask;
import util.MyPair;
import util.MyRandom;
import view.combat.MansionTheme;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.List;

public class InformationBrokerAssassinationEndingEvent extends AssassinationEndingEvent {
    public InformationBrokerAssassinationEndingEvent(Model model) {
        super(model, false, 0);
    }

    @Override
    protected void doEvent(Model model) {
        // This event is returned from LodgingNode, then called from AdvancedDailyActionState
        SubView alternativeSubView = new ImageSubView("theinn","EVENT", "The tavern.");
        CollapsingTransition.transition(model, alternativeSubView);
        AssassinationDestinationTask dt = AssassinationDestinationTask.getInfoBrokerTask(model);
        println("You quickly find a room occupied by a " + getRaceName(dt) + ".");
        leaderSay("Aha! " + dt.getWrit().getName() + " I presume!");
        GameCharacter victim = dt.getWrit().makeVictim();
        showExplicitPortrait(model, victim.getAppearance(), victim.getName());
        portraitSay("Oh, the assassins I've heard so much about. However did you find me?");
        leaderSay(iOrWeCap() + " got your little spy to spill the beans. Now say your prayers!");
        portraitSay("Darn it!");
        model.getLog().waitForAnimationToFinish();
        Ending ending = runCombatWithVictimInside(model, dt, victim, false);
        if (ending == Ending.success) {
            dt.setCompleted(true);
        } else {
            dt.setFailed(true);
        }
    }

    @Override
    protected void addConditionsToVictimEnemy(Model model, AssassinationDestinationTask task, FormerPartyMemberEnemy enemy,
                                              List<Enemy> companions, int victimFleeChance, boolean isOutside) {
        // No conditions.
    }

    @Override
    protected Ending enterHomePartTwo(Model model, AssassinationDestinationTask task, String place) {
        println("You search the " + place + " but find nothing but a few trinkets.");
        findSpareChange(model);
        leaderSay("Not a trace of " + task.getWrit().getName() + ".");
        GameCharacter talker = model.getParty().getLeader();
        if (model.getParty().size() > 1) {
            talker = model.getParty().getRandomPartyMember(talker);
        }
        partyMemberSay(talker, "Look's like nobody's been here for a week.");
        leaderSay("Aaah, crap. " + iOrWeCap() + "'ll never find " + himOrHer(task.getWrit().getGender()) + " now.#");
        return Ending.failure;
    }

    @Override
    protected Ending sneakAroundInside(Model model, AssassinationDestinationTask task, String place) {
        return enterHomePartTwo(model, task, place);
    }

    @Override
    public Ending leave(Model model, AssassinationDestinationTask task) {
        MyPair<SkillCheckResult, GameCharacter> pair = doPassiveSkillCheck(Skill.Perception, 7);
        if (!pair.first.isSuccessful()) {
            return super.leave(model, task);
        }
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Now is not the time for this.", "Something about this doesn't feel right.",
                        "No. We're not doing this.", "Hmm. Not today.", "Come on. We're leaving."));
        boolean kidGender = MyRandom.flipCoin();
        println("You are about to walk away from the " + task.getWrit().getDestinationShortDescription() +
                ", when you spot a child hiding in the bushes nearby. " + heOrSheCap(kidGender) +
                " was watching you intently.");
        leaderSay("Hey! You there!");
        println("You take a few quick steps and grab the kid before " + heOrShe(kidGender) + " can get away.");
        ChildAppearance app = PortraitSubView.makeChildAppearance(Race.ALL, kidGender);
        app.setEyebrowsDown();
        app.setBigMouth();
        showExplicitPortrait(model, app, "Kid");
        portraitSay("Hey, lemme go! Whadda ya want?");
        leaderSay("Why were you spying on " + meOrUs() + " kid?");
        portraitSay("Eat dung ya slug-nosed block head!");
        randomSayIfPersonality(PersonalityTrait.jovial, List.of(model.getParty().getLeader()), "Hehehe. This kid's got humor.");
        leaderSay("Just tell " + meOrUs() + " what you were doing, and " + iOrWe() + "'ll let you go.");
        String mister = task.getWrit().getGender() ? "Mizz " : "Mister ";
        model.getLog().waitForAnimationToFinish();
        app.setEyebrowsNormal();
        app.setNormalMouth();
        portraitSay(mister + task.getWrit().getLastName() + " said he was going away on a trip. " +
                "Gave me some money to keep a look out for any weirdos snooping around " +
                hisOrHer(task.getWrit().getGender()) + " house.");
        leaderSay("Oh... I understand. Well, I think there were some weirdos around here earlier... What were you supposed to do " +
                "if you saw any?");
        portraitSay(heOrSheCap(task.getWrit().getGender()) + " told me to go over to the tavern and tell the bartender.");
        leaderSay("I see. Okay. Well keep up the good work kid. Don't let " + mister + task.getWrit().getLastName() + " down!");
        portraitSay("Uhm... No I won't! See ya!");
        removePortraitSubView(model);
        println("Smiling. You leave the " + task.getWrit().getDestinationShortDescription() + ".");
        task.getWrit().setInfoBrokerFound(true);
        return Ending.unresolvedEndOfDay;
    }

    @Override
    protected Ending usePoisonPartTwo(Model model, AssassinationDestinationTask task, MyPair<String, String> objectAtDoor) {
        println("Several hours pass, but nobody emerges from the " + task.getWrit().getDestinationShortDescription() + ".");
        randomSayIfPersonality(PersonalityTrait.critical,
                List.of(model.getParty().getLeader()), "I don't want to say I told you so, but...");
        return getSpottedByWoman(model, task);
    }
}
