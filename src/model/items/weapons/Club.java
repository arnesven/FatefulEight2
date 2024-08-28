package model.items.weapons;

import model.items.Item;
import model.items.StartingItem;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Club extends BluntWeapon implements StartingItem, PairableWeapon{

    private static final Sprite SPRITE = new ItemSprite(0, 1);

    public Club() {
        super("Club", 5, new int[]{6, 10}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Club();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(10, 14);
    }
}
