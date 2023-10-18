package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Halberd extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 4);

    public Halberd() {
        super("Halberd", 23, new int[]{6,8,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Halberd();
    }
}
