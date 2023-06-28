package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.WeaponAvatarSprite;

public abstract class BluntWeapon extends Weapon {

    private static final WeaponAvatarSprite[] BLUNT_SPRITES = makeShiftedSpriteSet(
            new WeaponAvatarSprite(0x20, MyColors.BROWN, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE));
    private final boolean twoHander;
    private final int speedModifier;

    public BluntWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedModifier) {
        super(name, cost, Skill.BluntWeapons, damageTable);
        this.twoHander = twoHander;
        this.speedModifier = speedModifier;
    }

    @Override
    public int getSpeedModifier() {
        return speedModifier;
    }

    @Override
    public boolean isTwoHanded() {
        return twoHander;
    }

    @Override
    public String getSound() {
        return "wood";
    }

    @Override
    protected WeaponAvatarSprite getOnAvatarSprite(int index) {
        return BLUNT_SPRITES[index];
    }
}
