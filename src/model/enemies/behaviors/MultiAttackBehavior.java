package model.enemies.behaviors;

public class MultiAttackBehavior extends EnemyAttackBehavior {
    private int number;

    public MultiAttackBehavior(int number) {
        this.number = number;
    }

    @Override
    public boolean canAttackBackRow() {
        return false;
    }

    @Override
    public int numberOfAttacks() {
        return number;
    }

    @Override
    public String getUnderText() {
        return "Multi";
    }
}
