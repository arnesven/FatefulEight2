package view.help;

import model.combat.abilities.CombatAction;
import view.GameView;

public class TutorialFatigue extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character equipped with at least one piece of heavy armor can become fatigued during combat. " +
            "Beginning on combat round " + CombatAction.FATIGUE_START_ROUND + ", a character wearing heavy armor " +
            " who performs a fatiguing combat action must make an Endurance test with difficulty equal to that " +
            "character's armor points. If the test fails the character becomes fatigued. Subsequent usages of " +
            "fatiguing combat actions will exhaust 1 stamina point, or 1 health point.\n\n" +
            "All types of attacks in combat are fatiguing.";

    public TutorialFatigue(GameView view) {
        super(view, "Fatigue", TEXT);
    }
}
