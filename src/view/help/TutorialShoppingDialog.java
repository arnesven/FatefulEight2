package view.help;

import view.GameView;

public class TutorialShoppingDialog extends HelpDialog {
    private static final String text =
            "Merchants are found in many locations and are always eager to sell you their wares." +
            "In towns and at inns and castles, merchants trade at normal rates but may sell different " +
            "types of items. Merchants who appear in other locations may have different prices, so pay attention " +
            "to the cost and you may spot a bargain or avoid being swindled.\n\n" +
            "Some merchants will also buy goods from you. You can only sell items which are not equipped by any of" +
            " the characters in your party. The normal rate for selling is half the item's gold value.";

    public TutorialShoppingDialog(GameView view) {
        super(view, 25, "Shopping", text);
    }
}
