package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Kukri extends BladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(4, 11);

    public Kukri() {
        super("Kukri", 22, new int[]{5, 9}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Kukri();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public String getExtraText() {
        return "20% Critical Chance";
    }
}
