package model.items.weapons;

import model.items.Item;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Trident extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 4);

    public Trident() {
        super("Trident", 18, new int[]{8,8,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Trident();
    }
}
