package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Rapier extends BladedWeapon {

    private static final Sprite SPRITE = new ItemSprite(6, 12);

    public Rapier() {
        super("Rapier", 20, new int[]{6, 8, 10}, false, +1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Rapier();
    }
}
