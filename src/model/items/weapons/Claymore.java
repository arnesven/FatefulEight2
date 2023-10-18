package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Claymore extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 0);

    public Claymore() {
        super("Claymore", 24, new int[]{7,9,12,13}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Claymore();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
