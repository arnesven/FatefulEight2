package view.help;

import view.GameView;

public class TutorialEveningDialog extends HelpDialog {
    private static final String text =
            "Each evening your party must eat and find a place to sleep. Out in the wild you " +
            "will most often have to set up camp, consume some rations (1 per party member) " +
            " and sleep in the party's tent. Resting this way recovers 1 health point.\n\n" +
            "At various other locations there may be lodging available, albeit at a cost. A real meal, " +
            "and resting in a feather bed lets characters recover 2 health points and 1 stamina point.\n\n" +
            "Stamina points are used to re-roll skill checks.";

    public TutorialEveningDialog(GameView view) {
        super(view, 24, "Evening", text);
    }
}
