package model.items.weapons;

import model.classes.Skill;
import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class AxeWeapon extends Weapon {
    private static final AvatarItemSprite[] AXE_SPRITES = makeShiftedSpriteSet(new AvatarItemSprite(0x04, MyColors.BROWN, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BEIGE));
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
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return AXE_SPRITES[index];
    }
}
