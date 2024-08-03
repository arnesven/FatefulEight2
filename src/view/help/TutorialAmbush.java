package view.help;

import view.GameView;

public class TutorialAmbush extends SubChapterHelpDialog {
    private static final String TEXT =
            "Sometimes unfortunately, your enemies will have the upper hand. When enemies fall upon you unexpectedly, it is called an Ambush. " +
            "In Ambush combat you do not get to set your formation at start of battle and characters and allies do not get to act during the first round of combat.";

    public TutorialAmbush(GameView view) {
        super(view, "Ambush", TEXT);
    }
}
