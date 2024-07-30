package view.help;

import view.GameView;

public class TutorialDismiss extends HelpDialog {
    private static final String TEXT =
            "When recruiting characters, you also have the option to " +
            "dismiss characters who are already in your party.\n\n" +
            "When a character is dismissed from the party he or she will return any equipped gear but demand final payment. " +
            "Such demands can vary in size but normally increase with level of the character. If you cannot pay the " +
            "final payment in full, the character will instead keep all equipped gear.\n\n" +
            "If you have room at your headquarters, characters can be dismissed there instead of permanently leaving the party.";

    public TutorialDismiss(GameView view) {
        super(view, "Dismiss", TEXT);
    }
}
