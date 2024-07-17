package view.help;

import model.combat.conditions.CelerityVampireAbility;
import view.GameView;

import java.util.List;

public class TutorialVampires extends ExpandableHelpDialog {
    private static final String TEXT = "Vampires are dark creatures who pray on " +
            "the blood of the living to sustain themselves. Several events, " +
            "like being attacked by a vampire, or having your blood sucked by one, " +
            "can turn you into a vampire.\n\n" +
            "Characters who become vampires will be loathed and feared by others and will not " +
            "regenerate stamina like normal. For more information, see Vampirism Condition.\n\n" +
            "Vampires have a range of abilities at their disposal to augment them " +
            "in their dark endeavours, see the corresponding help chapters.\n\n" +
            "A character can be cured from vampirism through the magic of " +
            "ancient nature rituals, known only by some druids.";

    public TutorialVampires(GameView view) {
        super(view, "Vampires", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(CelerityVampireAbility.getHelpChapter(null));
    }
}
