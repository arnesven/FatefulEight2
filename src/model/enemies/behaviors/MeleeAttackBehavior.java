package model.enemies.behaviors;

public class MeleeAttackBehavior extends EnemyAttackBehavior {
    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public String getUnderText() {
        return "";
    }
}
