package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class OneHandedPolearmWeapon extends PolearmWeapon {

    private static final AvatarItemSprite POLEARM_SPRITES =
            new AvatarItemSprite(0x24, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BEIGE);

    public OneHandedPolearmWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable);
    }

    @Override
    public boolean isTwoHanded() {
        return false;
    }


    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return POLEARM_SPRITES;
    }

    @Override
    public int getStance() {
        return Weapon.NORMAL_STANCE;
    }
}
