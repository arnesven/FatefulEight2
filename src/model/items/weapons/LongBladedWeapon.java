package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class LongBladedWeapon extends BladedWeapon {

    protected static final AvatarItemSprite[] AVATAR_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x30, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));

    public LongBladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, damageTable, twoHander, speedBonus);
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return AVATAR_SPRITES[index];
    }
}
