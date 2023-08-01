package view.help;

import view.GameView;

public class TutorialCombatActionsDialog extends SubChapterHelpDialog {
    private static final String text =
            "Each combat round, characters take one combat action on their turn. Use the " +
            "arrow keys to select targets during combat.\n\n" +
            "Attack: The character uses their equipped weapon to attack the selected enemy. " +
            "Only ranged weapons can attack from the back row.\n\n" +
            "Item: Use an item from your inventory on the selected target.\n\n" +
            "Spell: Cast a Combat Spell on the selected target.\n\n" +
            "Flee: The party leader can announce a retreat from battle. If there is only " +
            "one character in your party, this has a 60% chance of success. Otherwise the " +
            "leader must succeed in a Leadership test where the difficulty is 3 + the number " +
            "of party members.\n\n" +
            "Pass: Do nothing in combat.";

    public TutorialCombatActionsDialog(GameView view) {
        super(view, "Actions", text);
    }
}
