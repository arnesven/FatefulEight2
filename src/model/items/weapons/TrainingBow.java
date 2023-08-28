package model.items.weapons;

import model.items.Item;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class TrainingBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(0, 7);

    public TrainingBow() {
        super("Training Bow", 5, new int[]{7,11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TrainingBow();
    }

    @Override
    public int getReloadSpeed() {
        return 5;
    }
}
