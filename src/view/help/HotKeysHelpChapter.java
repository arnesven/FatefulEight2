package view.help;

import view.GameView;
import view.MainGameViewHotKeyHandler;

public class HotKeysHelpChapter extends HelpDialog {
    private static final String TEXT =
            "Certain game functions can be triggered by the stroke of a key. " +
            "The following hot keys are available in the main game view:\n\n" +
            MainGameViewHotKeyHandler.getHotKeysAsText() +
            "Alt-Enter : Toggle fullscreen";

    public HotKeysHelpChapter(GameView view) {
        super(view, "Hotkeys", TEXT);
    }
}
