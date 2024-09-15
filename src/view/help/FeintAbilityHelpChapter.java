package view.help;

import model.actions.FeintAbility;
import view.GameView;

public class FeintAbilityHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT =  "A character with at least " + FeintAbility.BLADES_RANKS_REQUIREMENT + " ranks of Blades " +
            "can perform a Feint in combat. The character must have at least 1 Point of Stamina and " +
            "be equipped with a bladed weapon.\n\n" +
            "The feinting character performs a Blades Skill Check " + FeintAbility.DIFFICULTY + ". If successful the target will become " +
            "exposed until the end of the combat round and the feinting character immediately gets an attack against the target. " +
            "Performing a Feint is straining and exhausts " +
            "1 Stamina Point for the character.";;

    public FeintAbilityHelpChapter(GameView view) {
        super(view, "Feint", TEXT);
    }
}
