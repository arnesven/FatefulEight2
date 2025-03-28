package view.help;

import view.GameView;

public class TutorialFishing extends HelpDialog {
    private static final String TEXT =
            "If you have a fishing pole you can go fishing in rivers, lakes and oceans as a daily action.\n\n" +
            "Fishing uses Survival skill to determine if you catch anything. There are " +
            "several types of fish, some are more valuable and can feed more people.\n\n" +
            "When catching a fish you can immediately convert it to rations, or you can " +
            "do the conversion later from the Inventory Menu.\n\n" +
            "You can spend a little time fishing and still have time to travel that day. If you spend " +
            "a longer time, you increase your chances of catching fish, but cannot travel that day.\n\n" +
            "The number of attempts you get when fishing is dependent on your party size and the number of " +
            "fishing poles you have. However no party member can fish more than three times per day.";

    public TutorialFishing(GameView view) {
        super(view, "Fishing", TEXT);
    }
}
