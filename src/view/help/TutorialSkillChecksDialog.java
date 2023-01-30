package view.help;

import view.GameView;

public class TutorialSkillChecksDialog extends HelpDialog {
    private static final String text =
            "Skill checks occur when the party must overcome some sort of non-combat challenge. " +
            "During skill checks, characters test skills by roll a D10, adding their " +
            "skill bonus to that result and comparing it to a difficulty. A result of a natural 1 is always a failure. " +
            "Stamina points can be used to re-roll. " +
            "Successful checks give the character(s) experience points (XP).\n\n" +
            "Solo Checks: One party member must perform the skill check alone.\n\n" +
            "Collective Checks: All party members must pass the skill check for it to be successful.\n\n" +
            "Collaborative Checks: One party member is designated as the main performer of the skill check. " +
            "The other characters must pass a check of the same skill, but difficulty 7. Each other character " +
            "which passes this secondary check adds a +1 bonus to the main performer's roll. If that roll is successful, " +
            "the whole skill check is successful.";

    public TutorialSkillChecksDialog(GameView view) {
        super(view, "Skill Checks", text);
    }
}
