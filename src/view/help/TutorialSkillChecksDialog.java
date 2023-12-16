package view.help;

import model.classes.Skill;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class TutorialSkillChecksDialog extends ExpandableHelpDialog {
    private static final String text =
            "Skill checks occur when the party must overcome some sort of non-combat challenge. " +
            "During skill checks, characters test skills by rolling a D10, adding their " +
            "skill bonus to that result and comparing it to a difficulty. A result of a natural 1 is always a failure. " +
            "Stamina points can be used to re-roll. " +
            "Successful checks give the character(s) experience points (XP).\n\n" +
            "Solo Checks: One party member must perform the skill check alone.\n\n" +
            "Collective Checks: All party members must pass the skill check for it to be successful.\n\n" +
            "Collaborative Checks: One party member is designated as the main performer of the skill check. " +
            "The other characters must pass a check of the same skill, the difficulty of which depends " +
            "on the character's attitude towards the main performer. Each other character " +
            "which passes this secondary check adds a +1 bonus to the main performer's roll. If that roll is successful, " +
            "the whole skill check is successful.";

    public TutorialSkillChecksDialog(GameView view) {
        super(view, "Skill Checks", text);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> result = new ArrayList<>();
        for (Skill s : Skill.values()) {
            result.add(new SpecificSkillHelpDialog(view, s));
        }
        return result;
    }

}
