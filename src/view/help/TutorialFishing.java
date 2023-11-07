package view.help;

import view.GameView;

public class TutorialFishing extends HelpDialog {
    private static final String TEXT =
            "If you have a fishing pole you can go fishing in rivers, lakes and oceans.\n\n" +
            "Fishing uses Survival skill to determine if you catch anything. There are " +
            "several types of fish, some are more valuable and can feed more people.\n\n" +
            "When catching a fish you can immediately convert it to rations, or you can " +
            "do the conversion later from the Inventory Menu.";

    public TutorialFishing(GameView view) {
        super(view, "Fishing", TEXT);
    }
}
