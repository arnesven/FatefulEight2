package view.help;

import model.classes.Skill;
import view.GameView;

public class SpecificSkillHelpDialog extends SubChapterHelpDialog {
    public SpecificSkillHelpDialog(GameView view, Skill s) {
        super(view, 20, s.getName(), s.getDescription());
    }
}
