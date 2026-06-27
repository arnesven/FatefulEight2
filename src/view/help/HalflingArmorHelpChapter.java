package view.help;

import model.items.clothing.HalflingHeavyArmor;
import view.GameView;

public class HalflingArmorHelpChapter extends HelpDialog {

    private static final String TEXT = "Because of their small stature, Halflings sometimes have " +
            "difficulty donning heavy armor. There are however skilled Halfling smiths who produce " +
            "custom made armors which are much lighter (half) than their \"big-people\" counter part.\n\n" +
            "Heavy armor can be converted to Halfling armor through crafting (Customization) with a successful. " +
            "Labor Skill Check of " + HalflingHeavyArmor.CONVERT_SKILL_DIFFICULTY + ". " +
            "However, to convert the other " +
            "requires " + HalflingHeavyArmor.CONVERT_BACK_MATERIALS_COST + " materials.\n\nA failed conversion " +
            "attempt will ruin the armor permanently.";

    public HalflingArmorHelpChapter(GameView view) {
        super(view, "Halfling Armor", TEXT);
    }
}
