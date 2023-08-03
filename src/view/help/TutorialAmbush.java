package view.help;

import view.GameView;

public class TutorialAmbush extends SubChapterHelpDialog {
    private static final String TEXT =
            "Sometimes the you will surprise your enemies, springing combat on them " +
            "when they weren't expecting it. This is called an Ambush. During an ambush, " +
            "enemies will not get to take turns during the first combat round.";

    public TutorialAmbush(GameView view) {
        super(view, "Ambush", TEXT);
        setLevel(1);
    }
}
