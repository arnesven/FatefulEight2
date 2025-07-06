package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.StandardCombatLoot;

public abstract class VikingEnemy extends HumanoidEnemy {

    public VikingEnemy(char group, String name) {
        super(group, name);
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new StandardCombatLoot(model); // TODO: new VikingLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 5;
    }
}
