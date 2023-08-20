package model.enemies.behaviors;

public class RangedPoisonAttackBehavior extends PoisonAttackBehavior {

    public RangedPoisonAttackBehavior(int chance) {
        super(chance);
    }

    @Override
    public boolean canAttackBackRow() {
        return true;
    }
}
