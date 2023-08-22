package model.items.weapons;

import model.classes.Skill;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class NaturalWeapon extends Weapon {
    public NaturalWeapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost, skill, damageTable);
    }

    @Override
    protected Sprite getSprite() {
        return EMPTY_ITEM_SPRITE;
    }

    @Override
    public String getSound() {
        return "";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return null;
    }
}
