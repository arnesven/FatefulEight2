package model.items.clothing;

import model.items.Item;
import view.sprites.Sprite;

public class JustClothes extends Clothing {

    public JustClothes() {
        super("Clothes", 0, 0,false);
    }

    @Override
    protected Sprite getSprite() {
        return EMPTY_ITEM_SPRITE;
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("JustClothes should never be copied!");
    }
}
