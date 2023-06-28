package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.WeaponAvatarSprite;

public abstract class BladedWeapon extends Weapon {
    private static final WeaponAvatarSprite SWORD_SPRITES[] = makeShiftedSpriteSet(new WeaponAvatarSprite(0x0, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.PINK));
    private final int speedBonus;
    private boolean twoHander;

    public BladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, Skill.Blades, damageTable);
        this.twoHander = twoHander;
        this.speedBonus = speedBonus;
    }

    @Override
    public final boolean isTwoHanded() {
        return twoHander;
    }

    @Override
    public final int getSpeedModifier() {
        return speedBonus;
    }

    @Override
    public String getSound() {
        return "blade";
    }

    @Override
    protected WeaponAvatarSprite getOnAvatarSprite(int index) {
        return SWORD_SPRITES[index];
    }
}
