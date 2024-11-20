package view.subviews;

import model.Model;
import model.items.Item;
import model.items.MaterialsDummyItem;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

public class SalvageItemSubView extends SubView {
    private final Item itemToSalvage;
    private final SubView previous;
    private final MaterialsDummyItem materials;

    public SalvageItemSubView(SubView previous, Item itemToSalvage) {
        this.previous = previous;
        this.itemToSalvage = itemToSalvage;
        this.materials = new MaterialsDummyItem(0);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int y = Y_OFFSET + 22;
        itemToSalvage.drawYourself(model.getScreenHandler(), X_OFFSET + 6, y);
        BorderFrame.drawString(model.getScreenHandler(), itemToSalvage.getName(), X_OFFSET, y + 4, MyColors.WHITE);
        for (int i = 0; i < 2; ++i) {
            model.getScreenHandler().put(X_OFFSET + 15 + i, y+2, ArrowSprites.RIGHT_BLACK);
        }
        materials.drawYourself(model.getScreenHandler(), X_OFFSET + 22, y);
        BorderFrame.drawString(model.getScreenHandler(), "Materials", X_OFFSET+18, y + 4, MyColors.WHITE);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Salvaging " + itemToSalvage.getName() + "...";
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
