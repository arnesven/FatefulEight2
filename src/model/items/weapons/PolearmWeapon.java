package model.items.weapons;

import model.classes.Skill;

public abstract class PolearmWeapon extends Weapon {

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
}
