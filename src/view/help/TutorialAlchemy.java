package view.help;

import view.GameView;

public class TutorialAlchemy extends HelpDialog {
    private static final String TEXT =
            "The alchemy spell lets the caster convert gathered ingredients into " +
            "potions. Only potions which you already have in your inventory can be " +
            "brewed. The number of required ingredients is equal to half the potions " +
            "value.\n\n" +
            "Ingredients can be found in many locations in the world and can often be " +
            "collected from defeated monsters.";

    public TutorialAlchemy(GameView view) {
        super(view, "Alchemy", TEXT);
    }
}
