package model.items.accessories;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LargeShield extends ShieldItem {
    private static final Sprite SPRITE =  new ItemSprite(0, 3);

    public LargeShield() {
        super("Large Shield", 26, false, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LargeShield();
    }

    @Override
    public int getAP() {
        return 2;
    }
}
