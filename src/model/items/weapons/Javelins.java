package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Javelins extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 4);

    public Javelins() {
        super("Javelins", 16, new int[]{10,10,10});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Javelins();
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

}
