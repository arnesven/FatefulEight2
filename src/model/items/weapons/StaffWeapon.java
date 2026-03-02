package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class StaffWeapon extends BluntWeapon {
    private static final AvatarItemSprite STAFF_SPRITES =
            new AvatarItemSprite(0x23, MyColors.BROWN, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);

    public StaffWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable, true, 0);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return STAFF_SPRITES;
    }
}
