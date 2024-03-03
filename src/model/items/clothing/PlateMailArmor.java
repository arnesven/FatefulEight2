package model.items.clothing;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class PlateMailArmor extends HeavyArmorClothing {

    private static final Sprite SPRITE = new ItemSprite(11, 3,
            MyColors.BROWN, MyColors.RED, MyColors.LIGHT_PINK);

    public PlateMailArmor() {
        super("Plate Mail Armor", 36, 4);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PlateMailArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
