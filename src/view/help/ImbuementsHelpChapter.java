package view.help;

import view.GameView;

public class ImbuementsHelpChapter extends HelpDialog {
    private static final String TEXT = "Some weapons have magical imbuements. " +
            "Such items have an icon in the lower right corner.\n\n" +
            "Weapons can have at most one imbuement.";

    public ImbuementsHelpChapter(GameView view) {
        super(view, "Imbuements", TEXT);
    }
}
