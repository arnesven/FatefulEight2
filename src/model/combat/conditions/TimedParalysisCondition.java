package model.combat.conditions;

public class TimedParalysisCondition extends ParalysisCondition {

    public TimedParalysisCondition() {
        this(1);
    }

    public TimedParalysisCondition(int duration) {
        setDuration(duration + 1);
    }

}
