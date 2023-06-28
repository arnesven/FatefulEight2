package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class StaffWeapon extends BluntWeapon {
    private static final AvatarItemSprite[] STAFF_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x34, MyColors.BROWN, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE));

    public StaffWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable, true, 0);
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return STAFF_SPRITES[index];
    }
}
