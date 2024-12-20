package view.help;

import model.combat.abilities.CleaveAbility;
import view.GameView;

public class CleaveHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + CleaveAbility.AXE_RANKS_REQUIRED +
            " ranks of Axe Skill can perform the Cleave ability in combat while wielding an axe.\n\n" +
            "When performing the ability, the character first exhausts 1 Stamina, then makes a normal attack " +
            "against the targeted enemy, albeit with a damage bonus of 2. After the first attack up to two " +
            "additional enemies are dealt damage equal to half the damage suffered by the first target.";

    public CleaveHelpDialog(GameView view) {
        super(view, "Cleave", TEXT);
        setLevel(2);
    }
}
