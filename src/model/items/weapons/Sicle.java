package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Sicle extends AxeWeapon {

    private static final Sprite SPRITE = new ItemSprite(9, 12);

    public Sicle() {
        super("Sicle", 20, new int[]{7, 8, 8}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Sicle();
    }
}
