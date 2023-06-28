package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.WeaponAvatarSprite;

public abstract class BowWeapon extends Weapon {

    private static final WeaponAvatarSprite[] BOW_SPRITES = makeShiftedSpriteSet(
            new WeaponAvatarSprite(0x14, MyColors.BLACK, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE));

    public BowWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, Skill.Bows, damageTable);
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public boolean isTwoHanded() {
        return true;
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    @Override
    protected WeaponAvatarSprite getOnAvatarSprite(int index) {
        return BOW_SPRITES[index];
    }
}
