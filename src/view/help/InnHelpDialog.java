package view.help;

import view.GameView;

public class InnHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT = "Taverns which are located outside of towns are called Inns. " +
            "At inns you will sometimes find adventurers who want to join your party, and traveling merchants " +
            "who want to trade items with you. You can buy food and lodging at inns and taverns, as well as " +
            "buy or sell horses. At inns and taverns you can also take the time to socialize with the rest of your " +
            "party, to get a feeling of their attitudes toward one another and particularly, the party leader.";

    public InnHelpDialog(GameView view) {
        super(view, "Inns", TEXT);
    }
}
