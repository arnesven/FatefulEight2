package model.enemies.behaviors;

public class MultiKnockDownBehavior extends KnockDownAttackBehavior {
    private final int attacks;

    public MultiKnockDownBehavior(int attacks, int knockDownChance) {
        super(knockDownChance);
        this.attacks = attacks;
    }

    @Override
    public int numberOfAttacks() {
        return attacks;
    }

    @Override
    public String getUnderText() {
        return "Multi " + super.getUnderText();
    }
}
