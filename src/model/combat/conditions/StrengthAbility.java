package model.combat.conditions;

import model.classes.Skill;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class StrengthAbility extends VampireAbility {
    private static final int BONUS = 3;
    private static final String NAME = "Strength";
    private static final String DESCRIPTION = "Grants a permanent +" + BONUS + " to the Acrobatics and Endurance skills.";

    public StrengthAbility() {
        super(NAME, 0xA3, DESCRIPTION);
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.Acrobatics || skill == Skill.Endurance) {
            return BONUS;
        }
        return 0;
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, NAME, NAME + " is a vampire ability which " + DESCRIPTION.toLowerCase());
    }
}
