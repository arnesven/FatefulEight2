package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;

public abstract class CrossbowWeapon extends BowWeapon {
    private static final AvatarItemSprite ON_SPRITE = new FixedAvatarItemSprite(0x3C, MyColors.GRAY, MyColors.BROWN, MyColors.GRAY, MyColors.TRANSPARENT);

    public CrossbowWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, damageTable);
    }

    @Override
    public int getReloadSpeed() {
        return 8;
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return ON_SPRITE;
    }

    @Override
    public int getStance() {
        return Weapon.TWO_HANDED_STANCE;
    }
}
