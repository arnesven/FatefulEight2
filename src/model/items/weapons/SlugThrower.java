package model.items.weapons;

import model.classes.Skill;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.RangedStrikeEffect;
import view.sprites.RunOnceAnimationSprite;

public abstract class SlugThrower extends Weapon {
    private static final AvatarItemSprite[] ON_AVATAR_SPRITES = makeShiftedSpriteSet(new AvatarItemSprite(0x10,
            MyColors.BROWN, MyColors.BROWN, MyColors.BROWN, MyColors.PINK));

    private final boolean isTwoHanded;

    public SlugThrower(String name, int cost, int[] damageTable, boolean twoHanded) {
        super(name, cost, Skill.Perception, damageTable);
        this.isTwoHanded = twoHanded;
    }

    @Override
    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }

    public String getAttackSound() {
        return "slugthrower";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return ON_AVATAR_SPRITES[index];
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
