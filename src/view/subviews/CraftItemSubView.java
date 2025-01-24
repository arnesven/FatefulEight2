package view.subviews;

import model.Model;
import model.items.Item;
import model.items.MaterialsDummyItem;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

public class CraftItemSubView extends SubView {
    private final Item toCraft;
    private final MaterialsDummyItem cost;
    private final Boolean usingDesign;
    private final SubView previous;


    public CraftItemSubView(SubView previous, Item toCraft, int cost, Boolean usingDesign) {
        this.previous = previous;
        this.toCraft = toCraft;
        this.cost = new MaterialsDummyItem(cost);
        this.usingDesign = usingDesign;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int y = Y_OFFSET + 22;
        cost.drawYourself(model.getScreenHandler(), X_OFFSET + 6, y);
        BorderFrame.drawString(model.getScreenHandler(), cost.getName(), X_OFFSET, y + 4, MyColors.WHITE);
        for (int i = 0; i < 2; ++i) {
            model.getScreenHandler().put(X_OFFSET + 15 + i, y+2, ArrowSprites.RIGHT_BLACK);
        }
        toCraft.drawYourself(model.getScreenHandler(), X_OFFSET + 22, y);
        BorderFrame.drawString(model.getScreenHandler(), toCraft.getName(), X_OFFSET+18, y + 4, MyColors.WHITE);

        if (usingDesign) {
            BorderFrame.drawCentered(model.getScreenHandler(), "(Using Crafting Design)", y + 6, MyColors.WHITE);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "Crafting " + toCraft.getName() + ". Materials: " + model.getParty().getInventory().getMaterials();
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
