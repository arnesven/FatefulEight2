package view.help;

import model.headquarters.ExpandHeadquartersAction;
import model.headquarters.Headquarters;
import model.states.dailyaction.HeadquartersDailyActionState;
import view.GameView;

import java.util.List;

public class TutorialHeadquarters extends ExpandableHelpDialog {
    private static final String TEXT =
            "If you acquire real estate in a town or castle, the house will become the " +
            "headquarters for your party and replaces the 'Make Camp' action in town. " +
            "Spending a night at your there will consume rations, but sleeping in real " +
            "beds makes your party members recover stamina, just like staying at a tavern or inn.\n\n" +
            "At your headquarters you can store any amount of gold, rations, ingredients, materials and items. " +
            "You can also keep a number of horses and characters which you don't want in your party at the moment (see Sizes). " +
            "However, characters who stay at headquarters " +
            "will consume rations each day, so make sure you stock up. Characters who starve at headquarters " +
            "may leave permanently.\n\n" +
            "If you have vacant spaces at your headquarters you will have the option of sending characters there when you dismiss them.\n\n" +
            "Characters who stay at headquarters keep a log of what happens each day. You can read it when you visit there.\n\n" +
            "You can only have one headquarters.";

    public TutorialHeadquarters(GameView view) {
        super(view, "Headquarters", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new TutorialAssignments(view),
                new HeadquartersSizesHelpSection(view),
                new HeadquartersSubPartyHelpSection(view));
    }

    private static class HeadquartersSizesHelpSection extends SubChapterHelpDialog {
        private static final String SIZES_TEXT =
                "Headquarters come in five different sizes. Naturally, larger sizes are more costly.\n" +
                "The size of your headquarters " +
                "determines how many characters and horses can stay there while you are away on your adventures. " +
                "Headquarters size does not affect the amount of gold, food, ingredients, materials or items you can store.\n\n" +
                "SIZE     CHARS  HORSES  COST\n" +
                "Small        2       4   " + Headquarters.calcCostFor(Headquarters.SMALL_SIZE) + "\n" +
                "Medium       4       6   " + Headquarters.calcCostFor(Headquarters.MEDIUM_SIZE) + "\n" +
                "Large        6       8   " + Headquarters.calcCostFor(Headquarters.LARGE_SIZE) + "\n" +
                "Grand        8      10   " + Headquarters.calcCostFor(Headquarters.GRAND_SIZE) + "\n" +
                "Majestic    10      12   " + Headquarters.calcCostFor(Headquarters.MAJESTIC_SIZE) + "\n\n" +
                "If any character in your party has a Labor skill of " +
                        HeadquartersDailyActionState.LABOR_RANKS_REQUIRED_FOR_EXPAND + " or more, it is " +
                "possible to increase the size of your headquarters by selecting the 'Expand' action " +
                "while visiting there. The cost of making such an expansion is " +
                        ExpandHeadquartersAction.EXPAND_COST_MATERIALS + " materials and " +
                        ExpandHeadquartersAction.EXPAND_COST_GOLD + " gold.\n" +
                "Majestic headquarters can not be expanded.";

        public HeadquartersSizesHelpSection(GameView view) {
            super(view, "Sizes", SIZES_TEXT);
        }
    }
}
