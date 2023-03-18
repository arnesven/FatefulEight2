package model.classes;

import util.MyRandom;

public class SkillCheckResult {

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

    public SkillCheckResult(int rank, int difficulty) {
        this(rank, difficulty, 0);
    }

    public SkillCheckResult(int rank) {
        this(rank, Integer.MAX_VALUE, 0);
    }

    public int getModifiedRoll() {
        return modifiedRoll;
    }

    public String asString() {
        String extra = "";
        if (difficulty != Integer.MAX_VALUE) {
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

    public boolean isCritical() {
        return roll == 10;
    }
}
