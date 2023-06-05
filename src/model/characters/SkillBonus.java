package model.characters;

import util.MyPair;

public class SkillBonus extends MyPair<Integer, Boolean> {
    public SkillBonus(int bonus, boolean persistent) {
        super(bonus, persistent);
    }

    public int getBonus() { return first; }
    public boolean isPersistent() { return second; }
}
