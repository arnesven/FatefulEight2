package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Falchion extends BladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(7, 0);

    public Falchion() {
        super("Falchion", 16, new int[]{5, 8, 14}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Falchion();
    }
}
