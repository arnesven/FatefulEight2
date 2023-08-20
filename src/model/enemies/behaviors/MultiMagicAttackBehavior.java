package model.enemies.behaviors;

public class MultiMagicAttackBehavior extends EnemyAttackBehavior {
    private int number;

    public MultiMagicAttackBehavior(int number) {
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
    public boolean isPhysicalAttack() {
        return false;
    }

    @Override
    public String getUnderText() {
        return "Multi Magic";
    }
}
