package view.help;

import view.GameView;

import java.util.List;

public class TutorialRituals extends ExpandableHelpDialog {
    private static final String TEXT =
            "Rituals are events in which several mages come together to " +
            "perform a prolonged incantation with a powerful effect. The more mages who band together, " +
            "the more powerful the result of the ritual.\n\n" +
                    "The participants of a ritual stand " +
            "in a circle and attempt to establish beams between them. The object " +
            "of the ritual is to form a star shape with the beams. The star must have " +
            "the same number of points as participants of the ritual.\n\n" +
            "The success or failure of the ritual is dependent of the magical skills of the " +
            "participants. All your party members need not join the ritual, but the ones that do " +
            "will have to collaborate with mages from outside the party.\n\n" +
            "Rituals are taxing endeavours, and mages must " +
            "sometimes withdraw to avoid being fatally exhausted by the effects of the beam spells. " +
            "If the number of participants ever drop below five, the ritual automatically fails.";

    public TutorialRituals(GameView view) {
        super(view, "Rituals", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new TutorialBeams(view));
    }
}
