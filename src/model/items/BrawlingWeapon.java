package model.items;

import model.classes.Skill;
import model.items.weapons.PairableWeapon;
import model.items.weapons.Weapon;
import view.sprites.AvatarItemSprite;

public abstract class BrawlingWeapon extends Weapon implements PairableWeapon {

    private final boolean isTwoHanded;

    public BrawlingWeapon(String name, int cost, int[] damageTable, boolean isTwoHanded) {
        super(name, cost, Skill.UnarmedCombat, damageTable);
        this.isTwoHanded = isTwoHanded;
    }

    @Override
    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return null;
    }

    @Override
    public int getWeight() {
        return 500;
    }

    @Override
    public String getSound() {
        return "wood";
    }
}
