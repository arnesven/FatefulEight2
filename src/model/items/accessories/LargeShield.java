package model.items.accessories;

import model.items.Item;
import util.MyRandom;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LargeShield extends ShieldItem {
    private static final Sprite[] SPRITES =  new Sprite[]{new ItemSprite(0, 3),
        new ItemSprite(6, 3), new ItemSprite(7, 3)};
    private Sprite sprite;

    public LargeShield() {
        super("Large Shield", 26, false, 2);
        sprite = SPRITES[MyRandom.randInt(SPRITES.length)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new LargeShield();
    }

    @Override
    public int getAP() {
        return 2;
    }
}
