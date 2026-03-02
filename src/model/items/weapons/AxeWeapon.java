package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.CuttingStrikeEffectSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.RunOnceAnimationSprite;

public abstract class AxeWeapon extends Weapon {
    private static final AvatarItemSprite TWO_HANDED_AXE_SPRITES = new FixedAvatarItemSprite(0x4C, MyColors.BROWN, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BEIGE);
    private static final AvatarItemSprite AXE_SPRITES = new AvatarItemSprite(0x43, MyColors.BROWN, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BEIGE);
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
    public AvatarItemSprite getOnAvatarSprite() {
        if (isTwoHanded() && !isRangedAttack()) {
            return TWO_HANDED_AXE_SPRITES;
        }
        return AXE_SPRITES;
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new CuttingStrikeEffectSprite();
    }

    @Override
    public int getWeight() {
        return isTwoHanded() ? 5000 : 3000;
    }

    @Override
    public int getStance() {
        if (isTwoHanded() && !isRangedAttack()) {
            return TWO_HANDED_STANCE;
        }
        return NORMAL_STANCE;
    }
}
