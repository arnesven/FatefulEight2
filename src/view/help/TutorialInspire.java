package view.help;

import model.actions.InspireCombatAction;
import view.GameView;

public class TutorialInspire extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + InspireCombatAction.INSPIRE_SKILL_RANKS +
            " ranks of Leadership can perform the Inspire ability in combat. The character " +
            "performs a Leadership Skill check, which receives a +1 bonus if the character is currently " +
            "the party leader. Depending on the result the rest of the party is inspired and receives a " +
            "bonus to all attack rolls for two turns.\n\n" +
            "   Result     Bonus\n" +
            "    8-10        1\n" +
            "   11-13        2\n" +
            "    14+         3";

    public TutorialInspire(GameView view) {
        super(view, "Inspire", TEXT);
        setLevel(2);
    }
}
