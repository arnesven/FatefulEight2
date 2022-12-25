package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Pickaxe extends AxeWeapon {
    private static final Sprite SPRITE = new ItemSprite(2, 5);

    public Pickaxe() {
        super("Pickaxe", 14, new int[]{5, 8, 9}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Pickaxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
