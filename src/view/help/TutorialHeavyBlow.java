package view.help;

import model.actions.HeavyBlowCombatAction;
import view.GameView;

public class TutorialHeavyBlow extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + HeavyBlowCombatAction.LABOR_RANKS_REQUIREMENT + " of Labor " +
            "can perform a Heavy Blow in combat. The character must have at least 1 Point of Stamina and " +
            "be equipped with an axe or a blunt weapon.\n\n" +
            "A Heavy Blow acts as a normal attack but will do an additional 2 damage if it would " +
            "otherwise deal at least one damage to the target. This additional damage is added before " +
            "critical hit multiplier is applied. Performing a Heavy Blow is straining and exhausts " +
            "1 Stamina Point for the character.";

    public TutorialHeavyBlow(GameView view) {
        super(view, "Heavy Blow", TEXT);
        setLevel(2);
    }
}
