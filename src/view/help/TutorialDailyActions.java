package view.help;

import view.GameView;

public class TutorialDailyActions extends HelpDialog {

    private static final String text =
            "You will start each day by selecting your daily action. In some locations " +
            "you can select your daily action with the arrow keys, in others you are given " +
            "a choice as a textual list.\n\n" +
            "Some actions, like traveling, always take the whole day. Other actions, like " +
            "shopping and recruiting only take part of the day.\n\n" +
            "When taking the Stay In Hex action the party spends the day exploring their current " +
            "hex, then a new random event will be generated.\n\n" +
            "While in a hex with a road, you always have the option to get on or get off the road. " +
            "These actions are instantaneous and you may then select another daily action.";

    public TutorialDailyActions(GameView view) {
        super(view, "Daily Actions", text);
    }

}
