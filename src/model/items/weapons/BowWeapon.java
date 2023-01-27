package model.items.weapons;

import model.classes.Skill;

public abstract class BowWeapon extends Weapon {

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
}
