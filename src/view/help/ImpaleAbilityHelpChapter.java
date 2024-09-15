package view.help;

import model.actions.ImpaleAbility;
import view.GameView;

public class ImpaleAbilityHelpChapter extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + ImpaleAbility.POLEARMS_RANKS_REQUIREMENT + " ranks of Polearms " +
            "can perform the Impale ability in combat. The character must have at least 1 Point of Stamina and " +
            "be equipped with a polearm.\n\n" +
            "An impale attack acts as a normal attack but if the attack deals at least 1 damage to the target the target " +
            "will gain the Bleeding, Weakened, Exposed and Impaled conditions. " +
            "While impaled, the target cannot attack anybody but the character who impaled it. " +
            "The effect lasts between two to five combat rounds, during which the impaling combatant will become clinched with the target and can take no actions " +
            "(his or her weapon is stuck in the impaled combatant). When the target is no longer impaled, the weaken and exposed " +
            "conditions will be removed, while the bleeding will remain." +
            "\n\nPerforming an Impale attack is straining and exhausts " +
            "1 Stamina Point for the character.";

    public ImpaleAbilityHelpChapter(GameView view) {
        super(view, "Impale", TEXT);
        setLevel(2);
    }
}
