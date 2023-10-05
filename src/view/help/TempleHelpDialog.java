package view.help;

import view.GameView;

public class TempleHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "At temples you can partake in classes and martial training (see 'Training')\n\n" +
            "Mind your behavior when visiting a temple. Be sure not to offend the priests " +
            "or they may turn you away.\n\n" +
            "You can buy food and lodging at temples, as well as stock up on your rations.";

    public TempleHelpDialog(GameView view) {
        super(view, "Temples", TEXT);
    }
}
