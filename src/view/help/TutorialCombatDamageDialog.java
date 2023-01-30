package view.help;

import view.GameView;

public class TutorialCombatDamageDialog extends HelpDialog {
    private static final String text =
            "Each combat round, enemies will do damage to a random characters standing in the front row. " +
            "if there are now eligible targets in the front row, enemies will attack characters in " +
            "the back row. Damage dealt is subtracted from the characters Health Points (HP). If a character " +
            "ever reaches 0 HP, they are dead.\n\n" +
            "A character with armor has a chance to reduce incoming damage. A D10 is rolled, and the result is " +
            "subtracted from the character's Armor Points (AP), this is how much the damage is reduced by. Damage can " +
            "never be reduced to less than 0.";

    public TutorialCombatDamageDialog(GameView view) {
        super(view, "Combat - Damage", text);
    }
}
