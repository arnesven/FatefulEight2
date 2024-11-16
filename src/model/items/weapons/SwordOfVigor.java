package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.VigorImbuement;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SwordOfVigor extends BladedWeapon {

    private static final Sprite SPRITE = new ItemSprite(8, 0, MyColors.BLUE, MyColors.GOLD, MyColors.ORANGE);

    public SwordOfVigor() {
        super("Sword of Vigor", 110, new int[]{5, 8, 12}, false, 1);
        setImbuement(new VigorImbuement());
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
        return new SwordOfVigor();
    }
}
