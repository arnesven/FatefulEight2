package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.CuttingStrikeEffectSprite;
import view.sprites.RunOnceAnimationSprite;

public abstract class BladedWeapon extends Weapon {
    private static final AvatarItemSprite SWORD_SPRITES[] = makeShiftedSpriteSet(
            new AvatarItemSprite(0x0, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN, MyColors.PINK));
    protected static final AvatarItemSprite[] TWO_HANDED_SWORD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x30, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));

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
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        if (isTwoHanded()) {
            return TWO_HANDED_SWORD_SPRITES[index];
        }
        return SWORD_SPRITES[index];
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new CuttingStrikeEffectSprite();
    }

    @Override
    public int getWeight() {
        return isTwoHanded() ? 3000 : 1000;
    }
}
