package view.help;

import view.GameView;

public class TutorialTravelDialog extends HelpDialog {
    private static final String text =
            "Traveling is a full-day daily action and often results in resolving an event. " +
            "What event is generated depends on the type of terrain traveled to. Some events " +
            "result in combat. If you flee from combat during an event you will move to a random " +
            "hex adjacent to the one you traveled to.\n\n" +
            "Traveling on roads is generally safer than traveling through the rough, but is not a " +
            "significantly quicker way of traveling.\n\n"+
            "When crossing a river you must first resolve a river event to see if you may cross " +
            "at that particular location.\n\n" +
            "If you party is carrying more than your Carrying Capacity you will need to abandon " +
            "items and or resources before being able to venture forth.\n\n" +
            "For more information about different features of the world, see 'Terrain'.";

    public TutorialTravelDialog(GameView view) {
        super(view, "Traveling", text);
    }
}
