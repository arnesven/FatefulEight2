package model.combat.conditions;

import model.classes.Skill;

public class WisdomAbility extends VampireAbility {
    private static final int BONUS = 3;

    public WisdomAbility() {
        super("Wisdom", 0x93, "Grants a permanent +" + BONUS + " to the Logic and Perception skills.");
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.Logic || skill == Skill.Perception) {
            return BONUS;
        }
        return 0;
    }
}
