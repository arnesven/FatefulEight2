package view.help;

import model.states.duel.actions.MagicDuelAction;
import view.GameView;

public class ShieldingHelpSection extends SubChapterHelpDialog {
    private static final String TEXT = "Casting the shield spell during a magic duel " +
        "does not cost any magic power when cast on the standard level. Standard shields are capable of deflecting normal " +
        "attacks. Higher level shields are capable of deflecting special attacks, I.e. attacks of higher level, but have a " +
        "small power cost associated when them, proportionate to the level of shield.\n\n" +
        "The difficulty of casting the shield spell is " + MagicDuelAction.BASE_DIFFICULTY + " plus the level of the shield.";

    public ShieldingHelpSection(GameView view) {
        super(view, "Shielding", TEXT);
    }
}
