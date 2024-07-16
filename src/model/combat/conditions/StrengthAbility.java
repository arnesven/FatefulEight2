package model.combat.conditions;

import model.classes.Skill;

public class StrengthAbility extends VampireAbility {
    private static final int BONUS = 3;

    public StrengthAbility() {
        super("Strength", 0xA3,
                "Grants a permanent +" + BONUS + " to the Acrobatics and Endurance skills.");
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.Acrobatics || skill == Skill.Endurance) {
            return BONUS;
        }
        return 0;
    }
}
