package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SpikedKnuckles extends BrawlingWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.BLACK);

    public SpikedKnuckles() {
        super("Spiked Knuckles", 10, new int[]{5, 9}, false);
    }

    @Override
    public int getSpeedModifier() {
        return -1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SpikedKnuckles();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(2, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.BLACK);
    }
}
