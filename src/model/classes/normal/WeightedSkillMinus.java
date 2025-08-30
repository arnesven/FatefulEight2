package model.classes.normal;

import model.classes.Skill;
import model.classes.WeightedSkill;

public class WeightedSkillMinus extends WeightedSkill {
    public WeightedSkillMinus(Skill skill, int weight) {
        super(skill, weight);
    }

    @Override
    public int getRank(int level) {
        if (level <= 1) {
            return super.getRank(level);
        }
        return super.getRank(level - 1);
    }

    @Override
    public double getBalancingScore() {
        return super.getBalancingScore() * 0.7;
    }

    @Override
    public String asString() {
        return super.asString() + "-";
    }
}
