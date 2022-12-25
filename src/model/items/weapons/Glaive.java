package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Glaive extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 4);

    public Glaive() {
        super("Glaive", 24, new int[]{8,8,10,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Glaive();
    }
}
