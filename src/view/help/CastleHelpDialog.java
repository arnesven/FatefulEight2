package view.help;

import view.GameView;

public class CastleHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "Castles function as capital cities of their respective realms. Castles, like towns, are great places to " +
            "find quests, gear and people eager to join a party, but often also offer fun activities for your party to " +
            "take part in. Castles are generally pretty safe places and spending time there " +
            "is often fortuitous. If you are lucky you may even be granted an audience with the lord.";

    public CastleHelpDialog(GameView view) {
        super(view, "Castles", TEXT);
    }
}
