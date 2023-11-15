package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.combat.RowdyCondition;

public abstract class RowdyEnemyEnemy extends Enemy {
    public RowdyEnemyEnemy(char enemyGroup, String name) {
        super(enemyGroup, name);
        this.addCondition(new RowdyCondition());
    }

    @Override
    public int getMaxHP() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
