package view.help;

import model.states.events.GeneralInteractionEvent;
import view.GameView;

public class PickPocketing extends HelpDialog {
    private static final String TEXT =
            "During some events, you will have the opportunity to pick-pocket " +
            "people you come across. To pick-pocket you need at least two people " +
            "in your party, one to distract the mark, and one to perform the thieving.\n\n" +
            "When a character attempts pick-pocketing, they first tests Sneak with a difficulty of " +
            GeneralInteractionEvent.PICK_POCKETING_BASE_SNEAK_DIFFICULTY + " plus the mark's ranks of perception. " +
            "Should the first check be " +
            "successful the next test is against Security with a difficulty of " +
            GeneralInteractionEvent.PICK_POCKETING_BASE_SECURITY_DIFFICULTY + " plus the mark's level.\n\n"+
            "If either of the skill checks be unsuccessful it means the mark has detected the thief, " +
            "the party will gain Notoriety and the mark may attack you.\n\n" +
            "If however, both checks are successful you grab whatever is in the mark's pouch or " +
            "picket, without being noticed.";

    public PickPocketing(GameView view) {
        super(view, "Pick-Pocketing", TEXT);
    }
}
