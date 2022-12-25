package model.items.accessories;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ChainGloves extends GlovesItem {
    private static final Sprite SPRITE = new ItemSprite(4, 0xA, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.LIGHT_GRAY);

    public ChainGloves() {
        super("Chaing Gloves", 14);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ChainGloves();
    }

    @Override
    public int getAP() {
        return 1;
    }
}
