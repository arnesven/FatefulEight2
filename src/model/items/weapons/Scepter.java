package model.items.weapons;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Scepter extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(4, 1);

    public Scepter() {
        super("Scepter", 12, new int[]{5, 9, 13}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Scepter();
    }
}
