package view.help;

import model.classes.Skill;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class TutorialSkillChecksDialog extends ExpandableHelpDialog {
    private static final String MAIN_TEXT =
            "Skill checks occur when the party must overcome some sort of non-combat challenge. " +
            "During skill checks, characters test skills by rolling a D10, adding their " +
            "skill bonus to that result and comparing it to a difficulty. A result of a natural 1 is always a failure. " +
            "Stamina points can be used to re-roll skill checks. " +
            "Successful checks give the character(s) experience points (XP).\n\n" +
            "There are several types of skill checks; Solo, Collective, Collaborative and Reactive, see the corresponding help sections.";

    private static final String SOLO_TEXT =
            "In a Solo Skill Check one party member must perform the skill check alone.";

    private static final String COLLECTIVE_TEXT =
            "In a Collective Skill Check each party members must pass the skill check for it to be successful.\n\n" +
            "Collective Skill checks thus become more difficult the larger party you have.\n\n" +
            "Normally a Collective Skill Check fails as soon as one party member fails, " +
            "but sometimes Collective Skill Checks will go on until all party members have rolled, such skill checks are called " +
            "Exhaustive Collective Skill Checks.";

    private static final String COLLABORATIVE_TEXT =
            "In a Collaborative Skill Check, one party member is designated as the main performer of the skill check. " +
            "The other characters must pass a check of the same skill, the difficulty of which depends " +
            "on the character's attitude towards the main performer. Each other character " +
            "which passes this secondary check adds a +1 bonus to the main performer's roll. If that roll is successful, " +
            "the whole skill check is successful.\n\n" +
            "Collaborative Skill checks thus become more easy the larger party you have.";

    private static final String REACTIVE_TEXT = "Reactive Skill Checks occur in a reaction to something happening. " +
            "Often, such skill rolls will not be apparent (e.g. no die roll animation will occur), and the result may only " +
            "be displayed if the check is successful.\n\n" +
            "Stamina points can normally not be used for reactive checks.";

    private static final String SYNERGY_TEXT = "Sometimes a Skill Check in one skill may will gain a bonus from another. " +
            "In these cases the character's ranks of the other skill is added as a bonus to the die roll. Here are some " +
            "examples of Skill Checks which use synergies:\n\n" +
            "Intimidation checks use Persuade with a bonus from Endurance.\n\n" +
            "Casting a spell uses the Skill associated with the color of the spell with a bonus from Spell Casting.\n\n" +
            "Convincing a disgruntled party member to stay uses Persuade with a bonus from Leadership.";

    public TutorialSkillChecksDialog(GameView view) {
        super(view, "Skill Checks", MAIN_TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> result = new ArrayList<>();
        result.add(new SubChapterHelpDialog(view, "Collaborative", COLLABORATIVE_TEXT));
        result.add(new SubChapterHelpDialog(view, "Collective", COLLECTIVE_TEXT));
        result.add(new SubChapterHelpDialog(view, "Reactive", REACTIVE_TEXT));
        result.add(new SubChapterHelpDialog(view, "Solo", SOLO_TEXT));
        result.add(new SubChapterHelpDialog(view, "Synergy", SYNERGY_TEXT));
        return result;
    }

}
