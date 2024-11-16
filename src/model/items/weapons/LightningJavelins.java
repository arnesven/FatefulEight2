package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.ChainLightningImbuement;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class LightningJavelins extends PolearmWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 4, MyColors.BROWN, MyColors.CYAN, MyColors.BEIGE);

    public LightningJavelins() {
        super("Lightning Javelins", 128, new int[]{10, 10, 10});
        setImbuement(new ChainLightningImbuement());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public Item copy() {
        return new LightningJavelins();
    }
}
