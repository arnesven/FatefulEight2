package view.help;

import view.GameView;

public class TutorialCraftingDesigns extends SubChapterHelpDialog {
    private static final String TEXT =
            "Keep a lookout for Crafting Designs. With designs you don't need a copy of the item you want to craft, and " +
            "you don't need as many materials as you would normally (one third).";

    public TutorialCraftingDesigns(GameView view) {
        super(view, "Crafting Designs", TEXT);
    }
}
