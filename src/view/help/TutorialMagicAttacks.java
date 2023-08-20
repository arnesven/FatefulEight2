package view.help;

import view.GameView;

public class TutorialMagicAttacks extends SubChapterHelpDialog {
    private static final String TEXT =
            "Some enemies use magic attacks. Magic attacks completely ignore armor " +
            "and cannot be blocked by shields. They can however be evaded.";

    public TutorialMagicAttacks(GameView view) {
        super(view, "Magic Attacks", TEXT);
    }
}
