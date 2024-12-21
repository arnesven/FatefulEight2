package view.help;

import model.classes.Skill;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class SkillsHelpChapter extends ExpandableHelpDialog {

    private static final String TEXT =
            "Skills are a character's traits which describe how good that character is at performing certain tasks. " +
            "A character's Class's Skill Weights determines the number of ranks a character gets for each level gained. Consult the table " +
            "below to see how a ranks are related to Skill Weight and Level.\n\n";

    public SkillsHelpChapter(GameView view) {
        super(view, "Skills", makeText(), false);
    }

    private static String makeText() {
        StringBuilder bldr = new StringBuilder(TEXT);
        bldr.append("Level  W1 W2 W3 W4 W5 W6\n");
        for (int level = 0; level < Skill.RANK_MATRIX[0].length; ++level) {
            bldr.append("    ").append(level + 1).append("  ");
            for (int weight = 0; weight < Skill.RANK_MATRIX.length; ++weight) {
                bldr.append(Skill.RANK_MATRIX[weight][level] + "  ");
            }
            bldr.append("\n");
        }

        bldr.append("\n");
        bldr.append("Most skills have a governing attribute; Strength, Dexterity, Charisma or Wits.\n\n");
        bldr.append("Some skills grant Combat Abilities when a character reaches a required number of ranks.");

        return bldr.toString();
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
