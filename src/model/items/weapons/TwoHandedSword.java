package model.items.weapons;

import model.items.Item;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class TwoHandedSword extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(11, 0);

    public TwoHandedSword() {
        super("Two-Handed Sword", 24, new int[]{6, 10, 11, 14}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwoHandedSword();
    }
}
