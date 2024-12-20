package view.help;


import model.Model;
import model.actions.*;
import model.combat.abilities.*;
import model.combat.conditions.*;
import model.items.potions.CharismaPotion;
import model.items.potions.DexterityPotion;
import model.items.potions.StrengthPotion;
import model.items.potions.WitsPotion;
import model.items.spells.BlackPactCondition;
import model.items.spells.BoneArmorCondition;
import model.items.spells.QuickenedCondition;
import model.states.events.EnchantressEvent;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.List;

public class ConditionsHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT = "There are many conditions that may effect the well-being of a character. " +
            "Conditions are presented for each character as 'STATUS', and can also be viewed through the party menu.\n\n" +
            "Conditions have different durations. Some expire only a few combat rounds, some expire at end of combat. " +
            "Some conditions last until the end of the day. Some conditions are permanent, until specific circumstances come " +
            "about which remove them.\n\n" +
            "For an overview of all conditions, see the Summary page.";

    public ConditionsHelpChapter(GameView view) {
        super(view, "Conditions", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> result = new ArrayList<>();
        result.add(new ConditionSummaryHelpChapter(view, makeAllConditions()));
        for (Condition cond : makeAllConditions()) {
            result.add(cond.getHelpView(view));
        }
        return result;
    }

    private List<Condition> makeAllConditions() {
        return List.of(
                new BatFormCondition(),
                new BlackPactCondition(),
                new BleedingCondition(),
                new BlessedCondition(0),
                new BoneArmorCondition(0),
                new BurningCondition(null),
                new BurningWeaponCondition(),
                new CastingFullRoundSpellCondition(null, null, null, 0),
                new CharismaPotion().getCondition(),
                new ClinchedCondition(null, null),
                new CowardlyCondition(new ArrayList<>()),
                new DefendCombatAction().getCondition(),
                new DexterityPotion().getCondition(),
                EnchantressEvent.getCondition(),
                new ErodeCondition(),
                new ExposedCondition(),
                new FatigueCondition(),
                new GiantGrowthCondition(2),
                new ImpaledCondition(null),
                new IntoxicatedCondition(),
                new InspireCombatAction().getCondition(),
                new InvisibilityCondition(3),
                new CurseCombatAction().getPainCondition(),
                new ParalysisCondition(),
                new PoisonCondition(),
                new QuickenedCondition(5),
                new RegenerationCondition(3),
                new RiposteCombatAction().getCondition(),
                new RoutedCondition(),
                new ShiningAegisCondition(5),
                new SlowedCondition(-1, 0),
                new SneakAttackCondition(),
                new StrangenessCondition(-1),
                new StrengthPotion().getCondition(),
                new SummonCondition(null),
                new TamedCondition(),
                new VampirismCondition(VampirismCondition.NO_STAGE, 0),
                new WardCondition(),
                new WeakenCondition(),
                new WerewolfFormCondition(null, 0),
                new WitsPotion().getCondition()
        );
    }

    private static class ConditionSummaryHelpChapter extends SubChapterHelpDialog {
        private final List<Condition> allConditions;

        public ConditionSummaryHelpChapter(GameView previous, List<Condition> allConditions) {
            super(previous, "Summary", "");
            this.allConditions = allConditions;
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            List<DrawableObject> dobjs = super.buildDecorations(model, xStart, yStart);
            int count = 0;
            for (Condition cond : allConditions) {
                int finalY = yStart + count + 3;
                int column = 0;
                int half = allConditions.size() / 2 + 1;
                if (count >= half) {
                    column = 18;
                    finalY -= half;
                }
                dobjs.add(new DrawableObject(xStart+1+column, finalY) {
                    @Override
                    public void drawYourself(Model model, int x, int y) {
                        model.getScreenHandler().put(x, y, cond.getSymbol());
                    }
                });
                dobjs.add(new TextDecoration(cond.getName(), xStart+3+column,
                        finalY, MyColors.WHITE, MyColors.BLUE, false));
                count++;
            }
            return dobjs;
        }
    }
}
