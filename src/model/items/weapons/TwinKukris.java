package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class TwinKukris extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(6, 11);

    public TwinKukris() {
        super("Twin Kukris", 44, new int[]{5, 9}, true, 1);
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwinKukris();
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
        return "20% Critical Hit Chance";
    }
}
