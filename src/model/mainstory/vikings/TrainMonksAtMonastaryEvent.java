package model.mainstory.vikings;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.CombatAdvantage;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.events.VisitMonasteryEvent;
import util.MyPair;

import java.util.List;

public class TrainMonksAtMonastaryEvent extends DailyEventState {
    private CombatAdvantage combatAdvantage;
    private int trainingLevel;

    public TrainMonksAtMonastaryEvent(Model model) {
        super(model);
        this.combatAdvantage = CombatAdvantage.Neither;
        this.trainingLevel = 2;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You get up early and head over to the courtyard at the monastary. Many of the monks " +
                "have already gathered there. Some of them are talking to each other in hushed, worried voices. " +
                "Others are inspecting the gear you have brought them. None of them look very optimistic.");
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.PRI, "Sixth Monk");
        portraitSay("Hello there. We've assembled, as you can see. Ready to receive your instruction. " +
                "But tell me truthfully. What chances do the lot of us have against a raiding party of savage vikings?");
        talkToMonk(model);
        trainMonks(model);
    }

    private void talkToMonk(Model model) {
        List<String> replies = List.of(
                "You have the high ground",
                "Hope is your best ally",
                "Your chances are slim at best");
        int choice = multipleOptionArrowMenu(model, 24, 24, replies);
        leaderSay(replies.get(choice) + ".");
        if (choice == 0) {
            this.combatAdvantage = CombatAdvantage.Party;
            leaderSay("Properly defended, it will take more than a hundred to take this fortification.");
        } else if (choice == 1) {
            this.trainingLevel += 1;
            leaderSay("So find your courage friend.");
        } else {
            leaderSay("You should have taken my advice and evacuated.");
        }
        println("The monk swallows in a dry throat.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
    }

    private void trainMonks(Model model) {
        leaderSay("In any case. The vikings will be here soon, maybe even tomorrow. " +
                "Let's make the best of the time we have! Attention monks, you are now soldiers!");
        MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Leadership, 14);
        if (pair.first) {
            println(pair.second.getName() + " gives an inspiring speech which motives the monks.");
            trainingLevel += 2;
        } else {
            println(pair.second.getName() + " tries to motivate the monks, but " +
                    heOrShe(pair.second.getGender()) + " ends up just spouting nonsensical military jargon.");
        }
        println("What kind of drills do you want to conduct?");
        int choice2 = multipleOptionArrowMenu(model, 24, 24, List.of("Swords", "Axes", "Polearms", "Bows"));
        List<Skill> skills = List.of(Skill.Blades, Skill.Axes, Skill.Polearms, Skill.Bows);
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, skills.get(choice2), 14);
        print("You train the monks hard all day. ");
        if (success) {
            println("And you can honestly say that it seems like the monks have gotten better at fighting now.");
            trainingLevel += 2;
        } else {
            println("But if there is any improvement to the monks combat expertise, it is hardly apparent.");
        }
    }

    @Override
    protected GameState getEveningState(Model model) {
        return new BeforeDefendAgainstVikingRaidEveningState(model);
    }

    private class BeforeDefendAgainstVikingRaidEveningState extends EveningState {
        public BeforeDefendAgainstVikingRaidEveningState(Model model) {
            super(model, true, true, false);
        }

        @Override
        protected GameState nextState(Model model) {
            return new DefendAgainstVikingRaidEvent(model, combatAdvantage, trainingLevel);
        }
    }
}
