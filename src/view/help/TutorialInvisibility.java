package view.help;

import model.actions.InvisibilityCombatAction;
import view.GameView;

public class TutorialInvisibility extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + InvisibilityCombatAction.REQUIRED_RANKS + " of " +
            InvisibilityCombatAction.SKILL_TO_USE.getName() + " and equipped with a staff or wand can perform the Invisibility " +
            "ability on a party member or ally in combat.\n\n" +
            "The performer will test " + InvisibilityCombatAction.SKILL_TO_USE.getName() + ". If the result is " +
            InvisibilityCombatAction.DIFFICULTY + " or more, the target will turn invisible for a number of combat rounds depending on " +
            "the result of the skill check.";

    public TutorialInvisibility(GameView view) {
        super(view, "Invisibility", TEXT);
        setLevel(2);
    }
}
