package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.FrogmanLoot;
import model.enemies.behaviors.EnemyAttackBehavior;

public abstract class FrogmanEnemy extends Enemy {
    public FrogmanEnemy(char enemyGroup, String name, EnemyAttackBehavior behavior) {
        super(enemyGroup, name, behavior);
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

    @Override
    public String getDeathSound() {
        return "frogman_death";
    }
}
