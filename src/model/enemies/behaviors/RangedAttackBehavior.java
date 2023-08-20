package model.enemies.behaviors;

public class RangedAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return true;
    }
}
