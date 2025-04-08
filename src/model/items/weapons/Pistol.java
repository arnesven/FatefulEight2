package model.items.weapons;

import model.items.Item;
import model.items.PirateItem;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Pistol extends SlugThrower implements PairableWeapon, PirateItem {
    private static final Sprite SPRITE = new ItemSprite(12, 16);

    public Pistol() {
        super("Pistol", 42, new int[]{6, 6, 8, 8}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 800;
    }

    @Override
    public Item copy() {
        return new Pistol();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(13, 16);
    }
}
