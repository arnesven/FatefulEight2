package view.help;


import model.actions.*;
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

import java.util.ArrayList;
import java.util.List;

public class ConditionsHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT = "There are many conditions that may effect the well-being of a character. " +
            "Conditions are presented for each character as 'STATUS', and can also be viewed through the party menu.\n\n" +
            "Conditions have different durations. Some expire only a few combat rounds, some expire at end of combat. " +
            "Some conditions last until the end of the day. Some conditions are permanent, until specific circumstances come " +
            "about which remove them.";

    public ConditionsHelpChapter(GameView view) {
        super(view, "Conditions", TEXT);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new BlackPactCondition().getHelpView(view),
                new BleedingCondition().getHelpView(view),
                new BoneArmorCondition(0).getHelpView(view),
                new BurningCondition(null).getHelpView(view),
                new BurningWeaponCondition().getHelpView(view),
                new CharismaPotion().getCondition().getHelpView(view),
                new CowardlyCondition(new ArrayList<>()).getHelpView(view),
                new DefendCombatAction().getCondition().getHelpView(view),
                new DexterityPotion().getCondition().getHelpView(view),
                EnchantressEvent.getCondition().getHelpView(view),
                new ErodeCondition().getHelpView(view),
                new FatigueCondition().getHelpView(view),
                new GiantGrowthCondition(2).getHelpView(view),
                new InspireCombatAction().getCondition().getHelpView(view),
                new InvisibilityCondition(3).getHelpView(view),
                new CurseCombatAction().getPainCondition().getHelpView(view),
                new ParalysisCondition().getHelpView(view),
                new PoisonCondition().getHelpView(view),
                new QuickenedCondition(5).getHelpView(view),
                new RegenerationCondition(3).getHelpView(view),
                new RiposteCombatAction().getCondition().getHelpView(view),
                new RoutedCondition().getHelpView(view),
                new ShiningAegisCondition(5).getHelpView(view),
                new SneakAttackCondition().getHelpView(view),
                new StrengthPotion().getCondition().getHelpView(view),
                new SummonCondition(null).getHelpView(view),
                new WardCondition().getHelpView(view),
                new WeakenCondition().getHelpView(view),
                new WitsPotion().getCondition().getHelpView(view)
        );
    }
}
