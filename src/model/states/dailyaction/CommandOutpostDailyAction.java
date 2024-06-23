package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import model.states.battle.BattleState;
import view.subviews.PortraitSubView;

import java.util.List;

public class CommandOutpostDailyAction extends DailyAction {
    public CommandOutpostDailyAction(Model model) {
        super("Visit Outpost", new CommandOutpostDailyEventState(model));
    }

    private static class CommandOutpostDailyEventState extends DailyEventState {

        private final CharacterAppearance fieldGeneralAppearance = PortraitSubView.makeRandomPortrait(Classes.PAL);

        public CommandOutpostDailyEventState(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("You wander into the army camp. It is a large camp with tents, wagons and training dummies everywhere. " +
                    "There are plenty of soldiers about along with armor, weapons, clothes, provisions and everything " +
                    "else an army needs to sustain itself. There's a larger, more colorful tent up on a little hill. You " +
                    "assume this is the command tent. You walk up to it and step inside. A muscular " +
                    fieldGeneralAppearance.getRace().getName().toLowerCase() + " greets you as you enter.");
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
            portraitSay("Hello there! You must be the ones the Commander mentioned in his message. " +
                    "I've been expecting you.");
            leaderSay("Are you preparing for battle?");
            portraitSay("Yes. Our scouts have spotted the enemy's units. We're just about to move out and deploy in the field. " +
                    "You got here just in time!");
            leaderSay("Good. What can we help with?");
            portraitSay("Well, you can go join any unit you like. Or you can stay with me and direct the battle. " +
                    "Which would you prefer?");
            int choice = multipleOptionArrowMenu(model, 26, 26, List.of("Join a unit", "Direct the battle"));
            // TODO: Implement joining a unit
            leaderSay("I think we'll stay with you. I'm sure we'll have some tactical insights we could share.");
            portraitSay("All right! Let's go give the enemy a taste of our zeal!");
            BattleState battle = new BattleState(model);
            battle.run(model);
        }
    }
}
