package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BoStaff extends BluntWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(0, 4);

    public BoStaff() {
        super("Bo Staff", 24, new int[]{5, 10, 12, 12}, true, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoStaff();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
