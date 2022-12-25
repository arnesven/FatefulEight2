package model.items.weapons;

import model.classes.Skill;

public abstract class BluntWeapon extends Weapon {

    private final boolean twoHander;
    private final int speedModifier;

    public BluntWeapon(String name, int cost, int[] damageTable, boolean twoHander, int speedModifier) {
        super(name, cost, Skill.BluntWeapons, damageTable);
        this.twoHander = twoHander;
        this.speedModifier = speedModifier;
    }

    @Override
    public int getSpeedModifier() {
        return speedModifier;
    }

    @Override
    public boolean isTwoHanded() {
        return twoHander;
    }
}
