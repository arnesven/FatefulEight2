package view.help;

import view.GameView;

public class TutorialAlchemy extends HelpDialog {
    private static final String TEXT =
            "The alchemy spell lets the caster convert gathered ingredients into " +
            "potions (brewing) or vice versa (distilling). Only potions which you already have in your inventory can be " +
            "brewed. The number of required ingredients is equal to half the potions " +
            "value.\n\n" +
            "Ingredients can be found in many locations in the world and can often be " +
            "collected from defeated monsters. \n\n" +
            "Keep a lookout for Potion Recipes. With recipes, you don't need a draft of " +
            "the potion you want to brew, and using a recipe consumes less ingredients " +
            "then you would normally (one third).\n\n" +
            "When distilling a potion, there is a small chance of also learning the Potion Recipe for that potion.";

    public TutorialAlchemy(GameView view) {
        super(view, "Alchemy", TEXT);
    }
}
