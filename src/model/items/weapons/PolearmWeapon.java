package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;

public abstract class PolearmWeapon extends Weapon {

    private static final AvatarItemSprite[] POLEARM_SPRITES = makeShiftedSpriteSet(
            new FixedAvatarItemSprite( 0x90, MyColors.BROWN, MyColors.BROWN, MyColors.BROWN, MyColors.BROWN));

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
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return POLEARM_SPRITES[index];
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
