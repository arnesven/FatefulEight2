package view.help;

import model.map.locations.FortressAtUtmostEdgeLocation;
import view.GameView;

public class FortressAtUtmostEdgeHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT = "TODO: Text about FatUE.";

    public FortressAtUtmostEdgeHelpDialog(GameView view) {
        super(view, FortressAtUtmostEdgeLocation.NAME, TEXT);
    }
}
