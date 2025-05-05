package model.enemies.behaviors;

import util.MyRandom;

public class IncreasedCriticalMeleeAttackBehavior extends EnemyAttackBehavior {
    private final int amount;

    public IncreasedCriticalMeleeAttackBehavior(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public boolean isCriticalHit() {
        return MyRandom.rollD10() >= 10 - amount;
    }

    @Override
    public String getUnderText() {
        return "Increased Critical";
    }
}
