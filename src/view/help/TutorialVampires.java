package view.help;

import view.GameView;

public class TutorialVampires extends HelpDialog {
    private static final String TEXT = "Vampires are dark creatures who pray on " +
            "the blood of living creatures to sustain themselves. Several events, " +
            "like being attacked by a vampire, or having your blood sucked by one, " +
            "can turn you into a vampire. For more information, see Vampirism Condition.\n\n" +
            "Vampires have a range of abilities at their disposal to augment them " +
            "in their dark endeavours, see the corresponding help chapters.\n\n" +
            "A character can be cured from vampirism through the magic of " +
            "ancient nature rituals, known only by some druids.";

    public TutorialVampires(GameView view) {
        super(view, "Vampires", TEXT);
    }
}
