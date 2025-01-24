package view.help;

import view.GameView;

public class LearningHelpChapter extends HelpDialog {
    private static final String TEXT =
            "Spells, Potion Recipes and Crafting Designs can be learned permanently from the Inventory Menu. " +
            "When an item is permanently learned, it is removed from your inventory, so you can never lose it. " +
            "But you can still use that item just as if it were in your inventory.";

    public LearningHelpChapter(GameView view) {
        super(view, "Learning", TEXT);
    }
}
