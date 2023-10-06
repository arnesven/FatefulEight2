package view.help;

import view.GameView;

public class BrokenRoadTerrainHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "In some places the road has not been maintained and time " +
            "and the elements have chipped away at it. These roads can " +
            "be traveled along, but offer little more comfort " +
            "than hiking through the wild.";

    public BrokenRoadTerrainHelpDialog(GameView view) {
        super(view, "Broken Road", TEXT);
    }
}
