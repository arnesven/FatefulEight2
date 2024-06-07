package model.enemies.behaviors;

public class MultiRangedAttackBehavior extends RangedAttackBehavior{
    private final int attacks;

    public MultiRangedAttackBehavior(int attacks) {
        this.attacks = attacks;
    }

    @Override
    public int numberOfAttacks() {
        return attacks;
    }
}
