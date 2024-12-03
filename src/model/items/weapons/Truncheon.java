package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Truncheon extends BluntWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(0, 1);

    public Truncheon() {
        super("Truncheon", 10, new int[]{6, 10, 13}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Truncheon();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(10, 14);
    }
}
