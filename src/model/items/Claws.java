package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Claws extends BrawlingWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);

    public Claws() {
        super("Claws", 15, new int[]{7, 7, 13}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Claws();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(2, 8, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
