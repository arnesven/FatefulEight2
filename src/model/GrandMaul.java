package model;

import model.items.Item;
import model.items.Prevalence;
import model.items.weapons.BluntWeapon;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class GrandMaul extends BluntWeapon {
    private static final Sprite SPRITE = new ItemSprite(9, 1);

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
