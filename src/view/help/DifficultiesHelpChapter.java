package view.help;

import model.GameDifficulty;
import util.MyStrings;
import view.GameView;

public class DifficultiesHelpChapter extends HelpDialog {
    private static final String TEXT =
            "The game can be played at " + MyStrings.numberWord(GameDifficulty.values().length) +
                    " different difficulty levels.\n\n";

    public DifficultiesHelpChapter(GameView view) {
        super(view, "Difficulty", TEXT + makeDifficultyTexts());
    }

    private static String makeDifficultyTexts() {
        StringBuilder bldr = new StringBuilder();
        for (GameDifficulty diff : GameDifficulty.values()) {
            bldr.append(diff.toString());
            bldr.append(": ");
            bldr.append(diff.getDescription());
            bldr.append("\n\n");
        }
        bldr.append("Game Difficulty can be changed in the Settings View at any time.");
        return bldr.toString();
    }
}
