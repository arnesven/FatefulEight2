package view.help;

import model.actions.RegenerationCombatAction;
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
                new CombatAbilitiesChapter(),
                new TutorialAmbush(null),
                new TutorialCombatActionsDialog(null),
                new TutorialCombatAttacks(null),
                new TutorialBlocking(null),
                new TutorialCombatDamageDialog(null),
                new TutorialEnemyAttacks(null),
                new TutorialEvading(null),
                new TutorialFatigue(null),
                new TutorialCombatFormationDialog(null)
        );
    }

    private static class CombatAbilitiesChapter extends ExpandableHelpDialog {
        private static final String ABILITIES_TEXT =
                "Combat abilities are special actions which can be taken in combat if a character " +
                "fulfill certain criteria.";

        public CombatAbilitiesChapter() {
            super(null, "Abilities", ABILITIES_TEXT);
        }

        @Override
        public String getTitle() {
            return ((char)0x7D) + super.getTitle();
        }

        @Override
        protected List<HelpDialog> makeSubSections(GameView view) {
            return List.of(
                    new TutorialDefending(null),
                    new TutorialInspire(null),
                    new TutorialInvisibility(null),
                    new TutorialFairyHeal(null),
                    new TutorialHeavyBlow(null),
                    new TutorialMagicMissile(null),
                    new TutorialCombatResting(null),
                    new TutorialRegenerate(null),
                    new TutorialRiposte(null),
                    new TutorialSneakAttack(null),
                    new TutorialSniperShot(null)
            );
        }
    }
}
