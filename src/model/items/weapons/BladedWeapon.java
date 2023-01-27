package model.items.weapons;

import model.classes.Skill;

public abstract class BladedWeapon extends Weapon {
    private final int speedBonus;
    private boolean twoHander;

    public BladedWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedBonus) {
        super(name, cost, Skill.Blades, damageTable);
        this.twoHander = twoHander;
        this.speedBonus = speedBonus;
    }

    @Override
    public final boolean isTwoHanded() {
        return twoHander;
    }

    @Override
    public final int getSpeedModifier() {
        return speedBonus;
    }

    @Override
    public String getSound() {
        return "blade";
    }
}
