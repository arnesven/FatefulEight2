package view.help;

import model.states.duel.actions.MagicDuelAction;
import view.GameView;

public class DuelingAttacksHelpSection extends SubChapterHelpDialog {
    private static final String TEXT = "Attacks made during magic duels can " +
            "either be Normal or Special. When casting the duel attack spell the duelist " +
            "tests the Skill of the magic color used for that particular duel. The difficulty of the test is always " +
            MagicDuelAction.BASE_DIFFICULTY + ". If the opponent casts the attack spell at the same time the beams will " +
            "get caught in a Beam Lock.\n\n" +
            "A special attack is an attack with higher power. A normal attack consumes a little power from the Power Gauge but a special attack normally consumes all power from " +
            "the Power Gauge. A special attack which is not shielded or absorbed will deal more than one hit to " +
            "the opponent.\n\nA special attack that collides with an incoming attack from the opponent will raise the power of " +
            "the Beam Lock and potentially push it closer to the opponent. When this happens, some of the power spent to cast " +
            "the special attack is refunded to the duelist.";

    public DuelingAttacksHelpSection(GameView view) {
        super(view, "Dueling Attacks", TEXT);
    }
}
