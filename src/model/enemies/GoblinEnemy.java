package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;

public abstract class GoblinEnemy extends Enemy {
    public GoblinEnemy(char enemyGroup, String name, EnemyAttackBehavior behavior) {
        super(enemyGroup, name, behavior);
    }

    @Override
    public int getMaxHP() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public String getDeathSound() {
        return null;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    public abstract GoblinEnemy copy();
}
