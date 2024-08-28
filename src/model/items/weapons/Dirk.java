package model.items.weapons;

import model.items.Item;
import model.items.StartingItem;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Dirk extends SmallBladedWeapon implements StartingItem, PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(0, 0);

    public Dirk() {
        super("Dirk", 5, new int[]{5, 12}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Dirk();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(3, 14);
    }
}
