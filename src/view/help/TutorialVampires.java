package view.help;

import model.combat.conditions.*;
import view.GameView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TutorialVampires extends ExpandableHelpDialog {
    private static final String TEXT = "Vampires are dark creatures who pray on " +
            "the blood of the living to sustain themselves. Several events, " +
            "like being attacked by a vampire, or having your blood sucked by one, " +
            "can turn you into a vampire. Vampires are most common in rural areas where it is easy " +
            "for them to find suitable prey.\n\n" +
            "Characters who become vampires will be loathed and feared by others and will not " +
            "regenerate stamina like normal. For more information, see Vampirism Condition.\n\n" +
            "As their vampirism condition progresses, vampires can learn different special abilities to augment them " +
            "in their dark endeavours, see the corresponding help chapters.\n\n" +
            "A character can be cured from vampirism through the magic of " +
            "ancient nature rituals, known only by some druids.";

    public TutorialVampires(GameView view) {
        super(view, "Vampires", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<HelpDialog> helpDialogs = new ArrayList<>();
        for (VampireAbility vab : VampirismCondition.getAllAbilities()) {
            helpDialogs.add(vab.makeHelpChapter(view));
        }
        helpDialogs.add(new FeedingOnParty(view));
        helpDialogs.add(new FeedingOnFarm(view));
        helpDialogs.add(new TutorialFeedingTown(view));
        helpDialogs.add(new BloodTypesHelpChapter(view));
        helpDialogs.sort(Comparator.comparing(HelpDialog::getTitle));
        return helpDialogs;
    }
}
