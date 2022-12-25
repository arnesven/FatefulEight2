package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class HeraldicShield extends ShieldItem {
    private final ItemSprite sprite;

    public HeraldicShield() {
        super("Heraldic Shield", 16, true);
        this.sprite = new ItemSprite(3, 3,
                MyRandom.sample(List.of(MyColors.GRAY_RED, MyColors.DARK_RED, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.DARK_PURPLE)),
                MyRandom.sample(List.of(MyColors.PINK, MyColors.ORANGE, MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.CYAN)));
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new HeraldicShield();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
