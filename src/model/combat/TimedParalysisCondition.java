package model.combat;

public class TimedParalysisCondition extends ParalysisCondition {

    public TimedParalysisCondition() {
        this(1);
    }

    public TimedParalysisCondition(int duration) {
        setDuration(duration + 1);
    }

}
