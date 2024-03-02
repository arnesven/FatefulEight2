package view.help;

import view.GameView;

public class TutorialBasicControls extends HelpDialog {
    private static final String TEXT = "You can use the arrow keys and Enter to control the game." +
            "In some situations you may need the Y and N keys, as well as the space bar. " +
            "Scrolling is sometimes done with the PgUp and PgDn keys.\n\n" +
            "Pressing Alt and Enter will toggle full screen mode.\n\n" +
            "The Esc key opens the game menu, or exits the current menu. F1 opens the help section and F2 " +
            "displays the game log in a larger window.\n\nPlease set adjust the log speed to a desirable rate " +
            "in the settings menu.";

    public TutorialBasicControls(GameView view) {
        super(view, "Basic Controls", TEXT);
    }
}
