package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BecDeCorbin extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(8, 4);

    public BecDeCorbin() {
        super("Bec de corbin", 24, new int[]{6, 8, 10, 10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BecDeCorbin();
    }
}
