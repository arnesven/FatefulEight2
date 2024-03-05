package view.help;

import view.GameView;

public class TutorialGameLog extends HelpDialog {
    private static final String TEXT = "The game log can be viewed at the bottom of the screen. " +
            "By pressing the F2 button you can make the text area larger. Pressing F2 again will " +
            "enter full screen log mode.\n\n" +
            "In full screen log mode, you can scroll the log up and down with the up and down arrow. Or " +
            "scroll a whole page by pressing PgUp or PgDwn. You can export the contents of the log to a file " +
            "by pressing Control-P.\n\n" +
            "The printing speed of the text can be adjusted in the Settings menu. The 'Combat Speed' " +
            "determines the printing speed in combat, whereas 'Log Speed' is the printing speed outside " +
            "of combat.";

    public TutorialGameLog(GameView view) {
        super(view, "Game Log", TEXT);
    }
}
