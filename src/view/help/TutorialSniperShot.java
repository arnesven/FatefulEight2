package view.help;

import model.actions.SniperShotCombatAction;
import view.GameView;

public class TutorialSniperShot extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + SniperShotCombatAction.PERCEPTION_RANKS_REQUIREMENT +
            " ranks of Perception and armed with a ranged weapon can perform a Sniper Shot in combat. " +
            "The character must also have at least 1 Point of Stamina.\n\n" +
            "A Sniper Shot functions as a normal attack albeit with a 50% chance of critical hit instead of the " +
            "normal 10%. A character must focus intently while performing the ability and will thus exhaust " +
            "1 Stamina Point.";

    public TutorialSniperShot(GameView view) {
        super(view, "Sniper Shot", TEXT);
        setLevel(2);
    }
}
