package model.classes;

import util.MyRandom;

public class SkillCheckResult {

    public static final int NO_DIFFICULTY = Integer.MAX_VALUE;
    private final int roll;
    private final int modifiedRoll;
    private final int rank;
    private final int difficulty;
    private final int extraBonus;

    public SkillCheckResult(int rank, int difficulty, int extraBonus) {
        this.rank = rank;
        this.difficulty = difficulty;
        this.extraBonus = extraBonus;
        roll = MyRandom.rollD10();
        modifiedRoll = roll + rank + extraBonus;
    }

    public int getModifiedRoll() {
        return modifiedRoll;
    }

    public String asString() {
        String extra = "";
        if (difficulty != NO_DIFFICULTY) {
            if (isSuccessful()) {
                extra = " >=" + difficulty + " SUCCESS";
            } else {
                if (roll != 1) {
                    extra = " <" + difficulty + " FAIL";
                } else {
                    extra = " FAIL";
                }
            }
        }
        return "roll of " + roll + "+" + rank + (extraBonus > 0 ? ("+" + extraBonus) : "") + "=" + modifiedRoll + extra;
    }

    public boolean isSuccessful() {
        return modifiedRoll >= difficulty && roll != 1;
    }

    public boolean isCritical(int critLevel) {
        return roll >= critLevel;
    }

    public int getUnmodifiedRoll() {
        return roll;
    }

    public boolean isFailure() {
        if (difficulty == NO_DIFFICULTY) {
            return false;
        }
        return !isSuccessful();
    }
}
