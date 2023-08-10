package model.items.weapons;

import model.items.Item;
import util.MyRandom;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Hatchet extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(0, 5);
    private static final Sprite ALT_SPRITE = new ItemSprite(0, 11);
    private final Sprite sprite;

    public Hatchet() {
        super("Hatchet", 5, new int[]{6, 11}, false);
        if (MyRandom.flipCoin()) {
            sprite = SPRITE;
        } else {
            sprite = ALT_SPRITE;
        }
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new Hatchet();
    }
}
