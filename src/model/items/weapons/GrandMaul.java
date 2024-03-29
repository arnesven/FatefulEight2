package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class GrandMaul extends BluntWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(9, 1);

    public GrandMaul() {
        super("Grand Maul", 20, new int[]{7,9,12,13,15}, true, -2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GrandMaul();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
