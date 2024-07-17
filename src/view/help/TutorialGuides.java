package view.help;

import view.GameView;

public class TutorialGuides extends HelpDialog {
    private static final String TEXT =
            "Guides are individuals who can show you around, for a small fee.\n\n" +
            "In towns and castles, guides can sometimes be hired for the day and " +
            "offer to take you to one of several places.\n\n" +
            "At taverns you will sometimes find guides who you can hire to come into " +
            "the field with you. Such guides stay with you for an extended period of time " +
            "and can be useful for finding particular locations in the wilderness, e.g. " +
            "finding a good place to cross a river.";

    public TutorialGuides(GameView view) {
        super(view, "Guides", TEXT);
    }
}
