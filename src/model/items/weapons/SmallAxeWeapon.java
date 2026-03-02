package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class SmallAxeWeapon extends AxeWeapon {

    private static final AvatarItemSprite AXE_SPRITES =
            new AvatarItemSprite(0x40, MyColors.BROWN, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BEIGE);

    public SmallAxeWeapon(String name, int cost, int[] damageTable, boolean twoHander) {
        super(name, cost, damageTable, twoHander);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AXE_SPRITES;
    }
}
