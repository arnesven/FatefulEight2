package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;

public abstract class PolearmWeapon extends Weapon {

    private static final AvatarItemSprite POLEARM_SPRITES =
            new FixedAvatarItemSprite( 0x90, MyColors.BROWN, MyColors.BROWN, MyColors.TRANSPARENT, MyColors.TRANSPARENT);

    public PolearmWeapon(String name, int cost, int[] damageTable) {
        super(name, cost, Skill.Polearms, damageTable);
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
        return POLEARM_SPRITES;
    }

    @Override
    public int getWeight() {
        return 4000;
    }

    @Override
    public int getStance() {
        return POLEARM_STANCE;
    }
}
