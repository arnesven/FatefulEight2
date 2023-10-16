package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.RangedStrikeEffect;
import view.sprites.RunOnceAnimationSprite;

public abstract class WandWeapon extends Weapon {
    private static final AvatarItemSprite WAND_SPRITES[] = makeShiftedSpriteSet(new AvatarItemSprite(0x10,
            MyColors.BROWN, MyColors.BROWN, MyColors.BROWN, MyColors.PINK));

    public WandWeapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost, skill, damageTable);
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public Skill getSkillToUse(GameCharacter gc) {
        if (gc.getRankForSkill(super.getSkillToUse(gc)) < gc.getRankForSkill(Skill.MagicAny)) {
            return Skill.MagicAny;
        }
        return super.getSkillToUse(gc);
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return WAND_SPRITES[index];
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }

    @Override
    public int getWeight() {
        return 200;
    }
}
