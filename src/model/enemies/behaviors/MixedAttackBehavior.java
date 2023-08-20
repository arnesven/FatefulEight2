package model.enemies.behaviors;

import model.enemies.Enemy;

public class MixedAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public int calculateDamage(Enemy enemy, boolean isRanged) {
        int damage = super.calculateDamage(enemy, isRanged);
        if (isRanged) {
            return (int)(Math.ceil(((double)damage) / 2.0));
        }
        return damage;
    }
}
