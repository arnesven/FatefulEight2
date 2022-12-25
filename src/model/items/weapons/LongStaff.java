package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class LongStaff extends BluntWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(1, 1);

    public LongStaff() {
        super("Long Staff", 10, new int[]{5, 10}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LongStaff();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
