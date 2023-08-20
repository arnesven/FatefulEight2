package model.enemies;

import model.enemies.behaviors.EnemyAttackBehavior;

public abstract class UndeadEnemy extends Enemy {
    public UndeadEnemy(char enemyGroup, String name, EnemyAttackBehavior behavior) {
        super(enemyGroup, name, behavior);
    }
}
