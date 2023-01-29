package view.help;

import view.GameView;

public class TutorialDailyActions extends HelpDialog {

    private static final String text =
            "You will start each day by selecting your daily action. In some locations " +
            "you can select your daily action with the arrow keys, in others you are given " +
            "a choice as a textual list.\n\n" +
            "Some actions, like traveling, always take the whole day. Other actions, like " +
            "shopping and recruiting only take part of the day.";

    public TutorialDailyActions(GameView view) {
        super(view, "Daily Actions", text);
    }

}
