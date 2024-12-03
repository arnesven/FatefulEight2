package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class MakeshiftCrossbow extends CrossbowWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 7);

    public MakeshiftCrossbow() {
        super("Makeshift Crossbow", 18,  new int[]{8, 9, 12});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MakeshiftCrossbow();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
