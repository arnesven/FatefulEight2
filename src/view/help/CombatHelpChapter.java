package view.help;

import model.tutorial.TutorialCombatAttacks;
import view.GameView;

import java.util.List;

public class CombatHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT =
            "Combat occurs when the party encounters hostile persons or creatures." +
            "Combat progresses over a number of combat turns until either one side of " +
            "combatants have been wiped out or fled. In some situations combat can also " +
            "time out.\n\n" +
            "Whose turn it is during combat is depends on the initiative order, at the bottom of the combat display." +
            "Party members and allies take individual turns. Enemies take turns in groups. The initiative order " +
            "is constructed based on the combatants speed attributes.";

    public CombatHelpChapter(GameView view) {
        super(view, "Combat", TEXT);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new TutorialCombatActionsDialog(null),
                new TutorialCombatAttacks(null),
                new TutorialCombatDamageDialog(null),
                new TutorialCombatFormationDialog(null)
        );
    }
}
