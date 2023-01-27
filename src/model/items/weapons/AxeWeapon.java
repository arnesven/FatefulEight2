package model.items.weapons;

import model.classes.Skill;

public abstract class AxeWeapon extends Weapon {
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
}
