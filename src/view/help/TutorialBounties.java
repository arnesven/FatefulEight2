package view.help;

import model.tasks.BountyDestinationTask;
import view.GameView;

public class TutorialBounties extends HelpDialog {
    private static final String TEXT =
            "Bounties are a way for the authorities to make adventurers go " +
            "after wanted persons. Bounties can be learned about from wanted " +
            "posters in towns and castles.\n\n" +
            "Once you have accepted the bounty, you only have a face and a name, " +
            "so you will have to ask people you meet where the wanted " +
            "person can be found. Asking somebody about a bounty will require a successful Seek Info "
            + BountyDestinationTask.SEEK_INFO_DIFFICULTY + " skill check.\n\n"  +
            "If you are far way from the wanted person's " +
            "hiding place (more than " + BountyDestinationTask.LONG_RANGE + " hexes) " +
            "the person your asking will likely not know anything. If you " +
            "are close (between " + BountyDestinationTask.LONG_RANGE + " and " + BountyDestinationTask.SHORT_RANGE +
            " hexes) you may get an indication about it. If you are even closer than that, " +
            "you may get detailed directions to the wanted person's hideout.\n\n" +
            "Once you've located your target you must kill that person (capture is not an option), and return " +
            "to the turn-in location to receive your reward.";

    public TutorialBounties(GameView view) {
        super(view, "Bounties", TEXT);
    }
}
