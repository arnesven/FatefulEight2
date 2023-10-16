package model.items.weapons;

import model.classes.Skill;
import model.items.Item;

public class FamiliarWeapon extends NaturalWeapon {
    public FamiliarWeapon() {
        super("Claws", 0, Skill.UnarmedCombat, new int[]{5, 9});
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public Item copy() {
        return new FamiliarWeapon();
    }
}
