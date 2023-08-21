package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class TwinBladedWeapon extends BladedWeapon {
    private static final AvatarItemSprite[] TWIN_BLADES =  makeShiftedSpriteSet(
            new AvatarItemSprite(0x30, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));;

    public TwinBladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, damageTable, twoHander, speedBonus);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite(int index) {
        return TWIN_BLADES[index];
    }
}
