package view.help;

import view.GameView;

public class TutorialRobberies extends HelpDialog {
    private static final String TEXT =
            "When interacting with people you meet in the world you may choose to rob them of their valuables. " +
            "Doing so will increase your notoriety, more so than pickpocketing, but robbing somebody may be a more " +
            "direct way of getting their money without outright killing them.";

    public TutorialRobberies(GameView view) {
        super(view, "Robberies", TEXT);
    }
}
