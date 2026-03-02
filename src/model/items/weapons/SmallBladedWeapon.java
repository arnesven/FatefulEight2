package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class SmallBladedWeapon extends BladedWeapon {

    private static final AvatarItemSprite SMALL_BLADES =
            new AvatarItemSprite(0x50, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.BEIGE);

    public SmallBladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, damageTable, twoHander, speedBonus);
    }

    @Override
    public int getWeight() {
        return 500;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return SMALL_BLADES;
    }

}
