package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Shiv extends SmallBladedWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(0, 0, MyColors.PEACH, MyColors.GRAY, MyColors.PEACH);

    public Shiv() {
        super("Shiv", 7, new int[]{5, 10}, false, +1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Shiv();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(3, 14, MyColors.PEACH, MyColors.GRAY, MyColors.PEACH);
    }
}
