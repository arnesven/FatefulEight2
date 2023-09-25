package view.help;

import view.GameView;

public class TutorialObols extends HelpDialog {
    private static final String TEXT =
            "Obols is a currency used at inns and taverns around the world for gambling purposes. " +
            "Card games have a required minimum amount of obols needed to join in the game.\n\n" +
            "Talk to the bartender of the inn or tavern if you would like to buy or sell obols.\n\n" +
            "Obols are always traded at a flat rate of 10 obols to one gold.";

    public TutorialObols(GameView view) {
        super(view, "Obols", TEXT);
    }
}
