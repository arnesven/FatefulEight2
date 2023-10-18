package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Pike extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(6, 4);

    public Pike() {
        super("Pike", 34, new int[]{9,9,9,9,9,9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Pike();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
