package model.combat;

public class ParalysisCondition extends Condition {
    public ParalysisCondition() {
        super("Paralysis", "PAR");
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }
}
