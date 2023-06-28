package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.WeaponAvatarSprite;

public abstract class AxeWeapon extends Weapon {
    private static final WeaponAvatarSprite[] AXE_SPRITES = makeShiftedSpriteSet(new WeaponAvatarSprite(0x04, MyColors.BROWN, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BEIGE));
    private final boolean twoHander;

    public AxeWeapon(String name, int cost, int[] damageTable, boolean twoHander) {
        super(name, cost, Skill.Axes, damageTable);
        this.twoHander = twoHander;
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
        return AXE_SPRITES[index];
    }
}
