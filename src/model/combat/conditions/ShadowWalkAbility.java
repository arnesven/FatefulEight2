package model.combat.conditions;

import model.classes.Skill;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class ShadowWalkAbility extends VampireAbility {
    private static final int BONUS = 4;
    private static final String NAME = "Shadow Walk";
    private static final String DESCRIPTION = "Grants a permanent +" + BONUS + " to Sneaking.";

    public ShadowWalkAbility() {
        super("Shadow Walk", 0xA4, DESCRIPTION);
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, NAME, NAME + " is a vampire ability which " + DESCRIPTION.toLowerCase());
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.Sneak) {
            return BONUS;
        }
        return super.getBonusForSkill(skill);
    }
}
