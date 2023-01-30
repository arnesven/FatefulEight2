package view.help;

import view.GameView;

public class TutorialCombatFormationDialog extends HelpDialog {
    private static final String text =
            "Formation refers to the party's physical configuration during combat. " +
            "Use the arrow keys and SPACE to manipulate the configuration. " +
            "The formation consist of a front, and a back row. Characters in the front row " +
            "are exposed to attacks but can attack with melee weapons. Characters in the back " +
            "row are generally protected from attacks but can only attack if they have " +
            "ranged weapons. Characters in the back row can still perform other combat actions " +
            "like using items and casting spells.\n\n" +
            "Formation is configured each combat round. If however, there are more than twice " +
            "as many enemies than living characters in the front row, the party will be overrun, and " +
            "all characters are moved to the front.\n\n" +
            "Moving to the back row once combat has begun will provoke an attack of opportunity. " +
            "Attacks of opportunity inflict 1 damage on the character unless they pass an Acrobatics 7 " +
            "skill check.\n\n" +
            "The party's default configuration can be set from the Party Menu.";

    public TutorialCombatFormationDialog(GameView view) {
        super(view, "Combat - Formation", text);
    }
}
