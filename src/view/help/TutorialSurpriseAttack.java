package view.help;

import view.GameView;

public class TutorialSurpriseAttack extends SubChapterHelpDialog {
    private static final String TEXT =
            "Sometimes the you will surprise your enemies, springing combat on them " +
            "when they weren't expecting it. This is called an Surprise Attack. During a Surprise Attack, " +
            "enemies will not get to take turns during the first combat round.";

    public TutorialSurpriseAttack(GameView view) {
        super(view, "Surprise Attack", TEXT);
        setLevel(1);
    }
}
