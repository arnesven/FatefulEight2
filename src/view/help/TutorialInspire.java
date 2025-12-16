package view.help;

import model.combat.abilities.InspireCombatAction;
import view.GameView;

public class TutorialInspire extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + InspireCombatAction.LEADERSHIP_RANKS_REQUIREMENT +
            " ranks of Leadership can perform the Inspire ability in combat. The character " +
            "performs a Leadership Skill check, which receives a +1 bonus if the character is currently " +
            "the party leader. Depending on the result the rest of the party is inspired and receives a " +
            "bonus to all attack rolls for two turns.\n\n" +
            "   Result     Bonus\n" +
            "    " + InspireCombatAction.BASE_DIFFICULTY + "-10        1\n" +
            "   11-13        2\n" +
            "    14+         3\n\n" +
            "A character equipped with a Lute can perform the Ballad ability, which works like the Inspire " +
            "ability, but uses the Entertain skill and always gets the +1 bonus as if they were the party leader.";

    public TutorialInspire(GameView view) {
        super(view, "Inspire", TEXT);
        setLevel(2);
    }
}
