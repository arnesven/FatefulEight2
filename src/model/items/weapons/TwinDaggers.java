package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class TwinDaggers extends TwinBladedWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 11);

    public TwinDaggers() {
        super("Twin Daggers", 16, new int[]{4, 10}, true, 1);
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
        return new TwinDaggers();
    }
}
