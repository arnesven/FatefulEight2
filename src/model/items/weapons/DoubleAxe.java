package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class DoubleAxe extends AxeWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 5);

    public DoubleAxe() {
        super("Double Axe", 20, new int[]{6, 8, 10, 13}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DoubleAxe();
    }
}
