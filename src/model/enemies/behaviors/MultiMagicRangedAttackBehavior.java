package model.enemies.behaviors;

public class MultiMagicRangedAttackBehavior extends MagicRangedAttackBehavior {
    private int number;

    public MultiMagicRangedAttackBehavior(int number) {
        this.number = number;
    }

    @Override
    public int numberOfAttacks() {
        return this.number;
    }

    @Override
    public String getUnderText() {
        return "Multi " + super.getUnderText();
    }
}
