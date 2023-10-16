package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ObolsDummyItem extends InventoryDummyItem {
    private static final Sprite SPRITE = new ItemSprite(2, 12, MyColors.LIGHT_GRAY,
            MyColors.GRAY_RED, MyColors.CYAN);

    public ObolsDummyItem(int obols) {
        super("Obols (" + obols + ")", obols/10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", A sub-currency mostly used for gambling purposes.";
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
