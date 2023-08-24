package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CommonCrossbow extends CrossbowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 7);

    public CommonCrossbow() {
        super("Crossbow", 24, new int[]{8,9,10,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CommonCrossbow();
    }
}
