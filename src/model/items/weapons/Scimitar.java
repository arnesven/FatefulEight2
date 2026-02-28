package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Scimitar extends LongBladedWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 0);

    public Scimitar() {
        super("Scimitar", 17, new int[]{5, 9, 14}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Scimitar();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(7, 14);
    }
}
