package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ShortSpear extends PolearmWeapon {
    private static final Sprite SPRITE = new ItemSprite(7, 4);

    public ShortSpear() {
        super("Short Spear", 18, new int[]{8, 9, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    public boolean isTwoHanded() {
        return false;
    }

    @Override
    public Item copy() {
        return new ShortSpear();
    }
}
