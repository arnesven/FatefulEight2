package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class OneHandedPolearmWeapon extends PolearmWeapon {

    private static final AvatarItemSprite[] POLEARM_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x24, MyColors.BROWN, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE));

    public OneHandedPolearmWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable);
    }

    @Override
    public boolean isTwoHanded() {
        return false;
    }


    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return POLEARM_SPRITES[index];
    }

    @Override
    public int getStance() {
        return Weapon.NORMAL_STANCE;
    }
}
