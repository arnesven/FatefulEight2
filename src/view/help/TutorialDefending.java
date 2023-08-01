package view.help;

import view.GameView;

public class TutorialDefending extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least 3 ranks of Axes, Blades, Blunt Weapons or Polearms can " +
            "perform the Defend ability. The character enters a defensive stance and receives an " +
            "innate 20% block chance for the remainder of one combat round.";

    public TutorialDefending(GameView view) {
        super(view, "Defending", TEXT);
    }
}
