package model.enemies;

import model.enemies.behaviors.EnemyAttackBehavior;

import java.awt.Point;

public abstract class BigBeastEnemy extends BeastEnemy {
    public BigBeastEnemy(char enemyGroup, String name, int aggressiveness, EnemyAttackBehavior behavior) {
        super(enemyGroup, name, aggressiveness, behavior);
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public Point getCursorShift() {
        return new Point(2, 0);
    }
}
