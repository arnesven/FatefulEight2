package view.help;

import view.GameView;

public class RiverTerrainHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "In some places there are bridges permitting travelers to cross a river. In other places there may not be " +
            "a convenient or obvious way to cross a river. When crossing a river, adventurers will encounter " +
            "a river-based event which will determine if the river is crossed or not. Be aware, some rivers are deeper " +
            "than they appear and may have a strong undertow.";

    public RiverTerrainHelpDialog(GameView view) {
        super(view, "Rivers", TEXT);
    }
}
