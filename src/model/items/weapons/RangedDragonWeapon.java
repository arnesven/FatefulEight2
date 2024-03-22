package model.items.weapons;

import model.classes.Skill;
import model.items.Item;

public class RangedDragonWeapon extends NaturalWeapon {
    private final int numberOfAttacks;

    public RangedDragonWeapon(int numberOfAttacks) {
        super("Dragon Weapon", 0, Skill.UnarmedCombat, new int[]{7, 9, 11});
        this.numberOfAttacks = numberOfAttacks;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public int getNumberOfAttacks() {
        return numberOfAttacks;
    }

    @Override
    public Item copy() {
        return new RangedDragonWeapon(numberOfAttacks);
    }
}
