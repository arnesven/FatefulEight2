package view.help;

import model.actions.GrandSlamAbility;
import view.GameView;

public class GrandSlamHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + GrandSlamAbility.BLUNT_RANKS_REQUIRED + " ranks of " +
            "the Blunt Weapons skill, equipped with a blunt weapon and has at least 1 point of stamina, " +
            "can perform the Grand Slam ability in combat.\n\n" +
            "A Grand Slam acts as a normal attack but will do an additional 3 damage if it would " +
            "otherwise deal at least one damage to the target. This additional damage is added before " +
            "critical hit multiplier is applied. Whether or not any damage was dealt, the attack will stun " +
            "the enemy for 1 turn.\n\n" +
            "Performing a Grand Slam is straining and exhausts " +
            "1 Stamina Point for the character.";

    public GrandSlamHelpChapter(GameView view) {
        super(view, "Grand Slam", TEXT);
        setLevel(2);
    }
}
