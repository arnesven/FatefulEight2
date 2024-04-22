package view.help;

import model.states.events.MonumentEvent;
import model.states.events.VisitMonasteryEvent;
import view.GameView;

public class MonasteryHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "The monks of the Sixth Order are restoring a Monastery on the Isle of Faith. " +
            "If you happen to travel there you can stay there and rest for free as long as you like.\n\n" +
            "The monks want to make the Monastery into a safe haven for all lost travelers. Unfortunately " +
            "the once marvelous monastery has been reduced to a ruin by the tooth of time. They gladly accept " +
            "donations to once restore it to its former glory.\n\n" +
            "For every " + VisitMonasteryEvent.GOLD_PER_REP + " gold you donate to the Sixth Order " +
                    "Monks your reputation will increase by one.";

    public MonasteryHelpDialog(GameView view) {
        super(view, "Monastery", TEXT);
    }
}
