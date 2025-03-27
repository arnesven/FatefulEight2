package view.help;

import model.classes.prestige.PrestigeClass;
import view.GameView;

public class TutorialPrestigeClasses extends SubChapterHelpDialog {
    private static final String TEXT = "Prestige Classes are advanced classes that can only be attained " +
            "during special circumstances. Only characters of level " + PrestigeClass.MINIMUM_LEVEL +
            " or more can take on a prestige class.";

    public TutorialPrestigeClasses(GameView view) {
        super(view, "Prestige Classes", TEXT);
    }
}
