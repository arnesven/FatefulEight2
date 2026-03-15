package model.items.weapons;

import model.classes.Skill;
import view.sprites.BowAvatarItemSprite;
import view.sprites.RangedStrikeEffect;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.RunOnceAnimationSprite;

public abstract class BowWeapon extends Weapon {

    private static final AvatarItemSprite BOW_SPRITES = new BowAvatarItemSprite(0x30, MyColors.BLACK, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE);

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
    public AvatarItemSprite getOnAvatarSprite() {
        return BOW_SPRITES;
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }

    public int getReloadSpeed() {
        return 3;
    }

    @Override
    public int getWeight() {
        return getDamageTable().length * 200;
    }
}
