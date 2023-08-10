package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class TwinHatchets extends AxeWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 11);

    public TwinHatchets() {
        super("Twin Hatchets", 10, new int[]{6, 11}, true);
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwinHatchets();
    }
}
