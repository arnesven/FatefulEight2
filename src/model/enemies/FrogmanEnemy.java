package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.FrogmanLoot;

public abstract class FrogmanEnemy extends Enemy {
    public FrogmanEnemy(char enemyGroup, String name) {
        super(enemyGroup, name);
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new FrogmanLoot(model);
    }
}
