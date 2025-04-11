package model.items.special;

import model.items.Item;
import model.items.weapons.BluntWeapon;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class MagicBroom extends BluntWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(3, 17,
            MyColors.BROWN, MyColors.BEIGE, MyColors.BLACK);

    public MagicBroom() {
        super("Magic Broom", 20, new int[]{7, 10}, true, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MagicBroom();
    }

    @Override
    public String getExtraText() {
        return "Enables flying for single person.";
    }
}
