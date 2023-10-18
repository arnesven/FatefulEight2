package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Mace extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(8, 1);

    public Mace() {
        super("Mace", 21, new int[]{5, 8, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Mace();
    }
}
