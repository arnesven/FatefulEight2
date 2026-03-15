package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class LongBladedWeapon extends BladedWeapon {

    protected static final AvatarItemSprite AVATAR_SPRITES =
            new AvatarItemSprite(0x03, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    public LongBladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, damageTable, twoHander, speedBonus);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AVATAR_SPRITES;
    }
}
