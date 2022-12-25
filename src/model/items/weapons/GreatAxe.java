package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class GreatAxe extends AxeWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(5, 5);

    public GreatAxe() {
        super("Great Axe", 20, new int[]{5, 8, 10, 14}, true);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GreatAxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
