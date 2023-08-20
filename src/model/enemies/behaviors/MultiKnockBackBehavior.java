package model.enemies.behaviors;

public class MultiKnockBackBehavior extends KnockBackAttackBehavior {
    private final int number;

    public MultiKnockBackBehavior(int chance, int number) {
        super(chance);
        this.number = number;
    }

    @Override
    public int numberOfAttacks() {
        return number;
    }

    @Override
    public String getUnderText() {
        return "Multi " + super.getUnderText();
    }
}
