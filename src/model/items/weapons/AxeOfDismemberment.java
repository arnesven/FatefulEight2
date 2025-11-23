package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.DismembermentImbuement;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class AxeOfDismemberment extends AxeWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 5);

    public AxeOfDismemberment() {
        super("Axe of Dismemberment", 90, new int[]{6, 8, 10, 13}, true);
        setImbuement(new DismembermentImbuement());
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public String getExtraText() {
        return ", Chance of chopping of limbs from humanoid targets";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new AxeOfDismemberment();
    }
}
