package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Estoc extends LongBladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(11, 1);

    public Estoc() {
        super("Estoc", 28, new int[]{6, 8, 10, 12}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Estoc();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
