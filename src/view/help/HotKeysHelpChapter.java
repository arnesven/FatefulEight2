package view.help;

import view.GameView;

public class HotKeysHelpChapter extends HelpDialog {
    private static final String TEXT =
            "Certain game functions can be triggered by the stroke of a key. " +
            "The following hot keys are available in the main game view:\n\n" +
            "Ctrl-C : Party View\n" +
            "Ctrl-S : Skills View\n" +
            "Ctrl-Z : Spells View\n" +
            "Ctrl-W : Map View\n" +
            "Ctrl-Q : Journal View\n" +
            "Ctrl-X : Quit Game\n" +
            "Alt-Enter : Toggle fullscreen";

    public HotKeysHelpChapter(GameView view) {
        super(view, "Hotkeys", TEXT);
    }
}
