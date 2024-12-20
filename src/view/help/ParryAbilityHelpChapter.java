package view.help;

import model.combat.abilities.ParryAbility;
import view.GameView;

public class ParryAbilityHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + ParryAbility.BLADES_RANKS_REQUIREMENT + " ranks " +
            "of Blades has a chance to parry incoming physical (non-magic) attacks in combat.\n\n" +
            "The chance to parry an attack is proportional to the character's blade skill level.\n\n" +
            "    Skill   Chance\n" +
            "      3       10%\n" +
            "      4       20%\n" +
            "      5       30%\n" +
            "      6       40%\n" +
            "      7+      50%\n";

    public ParryAbilityHelpChapter(GameView view) {
        super(view, "Parry", TEXT);
        setLevel(2);
    }
}
