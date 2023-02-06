package model.combat;

public class ParalysisCondition extends Condition {
    public ParalysisCondition() {
        super("Paralysis", "PLZ");
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }
}
