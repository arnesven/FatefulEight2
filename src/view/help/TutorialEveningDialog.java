package view.help;

import view.GameView;

public class TutorialEveningDialog extends HelpDialog {
    private static final String text =
            "Each evening your party must eat and find a place to sleep. Out in the wild you " +
            "will most often have to set up camp, consume some rations (1 per party member) " +
            " and sleep in the party's tent. Resting this way recovers 1 health point.\n\n" +
            "If you do not have enough rations for your party everybody starves. When starving, " +
            "each character loses 1 Stamina Point if able, otherwise they lose 1 Health Point.\n\n" +
            "Rations can be purchased at inns and taverns (in Towns and Castles).\n\n" +
            "At various other locations there may be lodging available, albeit at a cost. A real meal, " +
            "and resting in a feather bed lets characters recover 2 Health Points, 1 Stamina Point and " +
            "can remove some negative conditions.\n\n" +
            "Stamina points are used to re-roll skill checks.";

    public TutorialEveningDialog(GameView view) {
        super(view, "Evening", text);
    }
}
