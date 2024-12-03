package model.items.accessories;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CrudeShield extends ShieldItem {

    public static final Sprite SPRITE = new ItemSprite(6, 3);

    public CrudeShield() {
        super("Crude Shield", 8, false, 1);
    }

    @Override
    public int getSpeedModifier() {
        return -1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CrudeShield();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public int getAP() {
        return 1;
    }
}
