package model.enemies.behaviors;

import model.enemies.Enemy;

public class DeadlyStrikeBehavior extends EnemyAttackBehavior{
    private final double multiplier;

    public DeadlyStrikeBehavior(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public int calculateDamage(Enemy enemy, boolean isRanged) {
        return (int)(super.calculateDamage(enemy, isRanged) * multiplier);
    }

    @Override
    public String getUnderText() {
        return "Ferocious";
    }
}
