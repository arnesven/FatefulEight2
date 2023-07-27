package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class RepeatingCrossbow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 7);

    public RepeatingCrossbow() {
        super("Repeating Crossbow", 24, new int[]{7, 9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RepeatingCrossbow();
    }

    @Override
    public String getExtraText() {
        return super.getExtraText();
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
