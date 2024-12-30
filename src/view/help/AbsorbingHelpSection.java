package view.help;

import model.states.duel.actions.MagicDuelAction;
import view.GameView;

public class AbsorbingHelpSection extends SubChapterHelpDialog {
    private static final String TEXT = "A duelist may attempt to absorb an incoming attack. " +
            "There is however a up front power cost to such absorption attempts. If the incoming " +
            "attack is successfully absorbed, the duelist will gain an amount of Power proportionate to " +
            "the level of the incoming attack. I.e. special attacks will generate more power when absorbed, whereas normal attack " +
            "only generate a little.\n\n" +
            "The difficulty of casting the absorption spell is " +
            MagicDuelAction.BASE_DIFFICULTY + " plus the power level of the incoming attack.";

    public AbsorbingHelpSection(GameView view) {
        super(view, "Absorbing", TEXT);
    }
}
