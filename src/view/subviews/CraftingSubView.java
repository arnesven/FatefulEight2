package view.subviews;

import model.Model;
import view.widget.SpriteArray;


public class CraftingSubView extends SubView {

    private static final SpriteArray SPRITE = new WorkBenchSprite();

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        SPRITE.drawYourself(model.getScreenHandler(), X_OFFSET, Y_OFFSET);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Crafting...";
    }

    @Override
    protected String getTitleText(Model model) {
        return "WORKBENCH";
    }

    private static class WorkBenchSprite extends SpriteArray {
        public WorkBenchSprite() {
            super("workbenchsubview", "crafting.png", 256, 164, true);
        }
    }
}
