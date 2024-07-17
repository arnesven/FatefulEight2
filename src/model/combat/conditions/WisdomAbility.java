package model.combat.conditions;

import model.classes.Skill;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class WisdomAbility extends VampireAbility {
    private static final int BONUS = 3;
    private static final String NAME = "Wisdom";
    private static final String DESCRIPTION = "Grants a permanent +" + BONUS + " to the Logic and Perception skills.";

    public WisdomAbility() {
        super(NAME, 0x93, DESCRIPTION);
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.Logic || skill == Skill.Perception) {
            return BONUS;
        }
        return 0;
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, NAME, NAME + " is a vampire ability which " + DESCRIPTION.toLowerCase());
    }
}
