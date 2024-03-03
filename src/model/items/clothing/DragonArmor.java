package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DragonArmor extends HeavyArmorClothing {
    private static final Sprite SPRITE = new ItemSprite(6, 2, MyColors.GRAY_RED, MyColors.BEIGE);

    public DragonArmor() {
        super("Dragon Armor", 34, 4);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DragonArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
