package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class StaffOfDeimosItem extends StaffWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 12, MyColors.DARK_RED, MyColors.TAN, MyColors.CYAN);

    public StaffOfDeimosItem() {
        super("Staff of Deimos", 3000, new int[]{6, 10});
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new StaffOfDeimosItem();
    }
}
