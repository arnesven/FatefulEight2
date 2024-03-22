package model.items.weapons;

import model.classes.Skill;
import model.items.Item;

public class MeleeDragonWeapon extends NaturalWeapon {
    public MeleeDragonWeapon(int[] damageTable) {
        super("Dragon Weapon", 0, Skill.UnarmedCombat, damageTable);
    }

    public MeleeDragonWeapon() {
        this(new int[]{6, 7, 9, 10});
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public Item copy() {
        return new MeleeDragonWeapon();
    }
}
