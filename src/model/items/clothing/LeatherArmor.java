package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LeatherArmor extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(3, 2);

    public LeatherArmor() {
        super("Leather Armor", 30, 3, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LeatherArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
