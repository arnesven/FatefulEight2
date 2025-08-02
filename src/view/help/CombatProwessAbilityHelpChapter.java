package view.help;

import model.combat.abilities.CombatProwessAbility;
import view.GameView;

public class CombatProwessAbilityHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT = "If a character has multiple melee attacks in combat, and has " +
            "attacks remaining after the target is eliminated, remaining attacks can be " +
            "directed toward other targets if the character possesses the Combat Prowess ability. " +
            "A character must have reached level " + CombatProwessAbility.REQUIRED_LEVEL +
            " and needs at least " +
            CombatProwessAbility.REQUIRED_ACROBATICS_RANKS + " to use Combat Prowess.\n\n" +
            "Other targets are randomly selected from enemies still remaining in the combat.";

    public CombatProwessAbilityHelpChapter(GameView view) {
        super(view, "Combat Prowess", TEXT);
        setLevel(2);
    }
}
