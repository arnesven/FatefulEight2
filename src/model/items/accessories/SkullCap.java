package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SkullCap extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(13, 9, MyColors.LIGHT_GRAY, MyColors.GOLD);

    public SkullCap() {
        super("Skull Cap", 12);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SkullCap();
    }

    @Override
    public int getAP() {
        return 1;
    }

    @Override
    public boolean isHeavy() {
        return true;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
