package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Longsword extends BladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(9, 0);

    public Longsword() {
        super("Longsword", 16, new int[]{6, 8, 13}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Longsword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
