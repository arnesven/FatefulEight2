package model.items.weapons;

import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class YewBow extends BowWeapon {
    private final TwoHandedItemSprite sprite;

    public YewBow() {
        super("Yew Bow", 21, new int[]{7,11,13,15});
        MyColors color = MyRandom.sample(List.of(MyColors.GREEN, MyColors.YELLOW, MyColors.LIGHT_BLUE, MyColors.TAN));
        sprite = new TwoHandedItemSprite(5, 7, MyColors.BROWN, MyColors.LIGHT_GRAY, color);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new YewBow();
    }
}
