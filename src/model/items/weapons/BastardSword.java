package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BastardSword extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(13, 0);

    public BastardSword() {
        super("Bastard Sword", 30, new int[]{5,6,13,13,14}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BastardSword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
