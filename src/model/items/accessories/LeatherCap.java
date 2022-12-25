package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LeatherCap extends HeadGearItem {
    private static final Sprite SPRITE = new ItemSprite(9, 10, MyColors.DARK_BROWN, MyColors.BROWN);

    public LeatherCap() {
        super("Leather Cap", 14);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LeatherCap();
    }

    @Override
    public int getAP() {
        return 1;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
