package model.items.weapons;

import model.items.Item;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Katana extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(10, 0);

    public Katana() {
        super("Katana", 26, new int[]{9, 10, 12, 12}, true, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Katana();
    }
}
