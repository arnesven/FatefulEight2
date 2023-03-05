package model.combat;

public class PoisonCondition extends Condition {
    public PoisonCondition() {
        super("Poison", "PSN");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }
}
