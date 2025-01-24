package view.help;

import view.GameView;

public class TutorialCraftingDesigns extends SubChapterHelpDialog {
    private static final String TEXT =
            "Keep a lookout for Crafting Designs. Crafting with designs is advantageous in several ways:\n\n" +
            "-You don't need a copy of the item you want to craft\n\n" +
            "-You don't need as many materials as you would normally (one third).\n\n" +
            "-You skip over the Perception skill check when crafting with designs, greatly increasing " +
            "the probability of successfully crafting the item.\n\n" +
            "-The probability of successfully crafting the item (without re-rolls) can be " +
            "seen in an analysis dialog from the Inventory Menu.\n\n" +
            "Crafting Designs can be learned permanently, see Learning.";

    public TutorialCraftingDesigns(GameView view) {
        super(view, "Crafting Designs", TEXT);
    }
}
