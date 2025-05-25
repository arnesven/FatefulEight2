package model.classes.normal;

import model.classes.Skill;
import model.classes.WeightedSkill;

public class WeightedSkillPlus extends WeightedSkill {

    public WeightedSkillPlus(Skill skill, int weight) {
        super(skill, weight);
    }

    @Override
    public int getRank(int level) {
        return super.getRank(level + 1);
    }

    @Override
    public double getBalancingScore() {
        return super.getBalancingScore() * 1.3;
    }

    @Override
    public String asString() {
        return super.asString() + "+";
    }
}
