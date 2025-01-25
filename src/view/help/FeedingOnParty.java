package view.help;

import model.states.events.VampireProwlNightEvent;
import view.GameView;

public class FeedingOnParty extends SubChapterHelpDialog {
    private static final String TEXT = "Vampires must feed on living humanoid beings to replenish their stamina. " +
            "Vampires in parties may be tempted to feed on other members of the party, but doing so may be risky!\n\n" +
            "When feeding on a party member, the vampire must first avoid detection by all the other party members, by succeeding " +
            "in a sneak roll of 3 + number of party members. Then, the victim may still notice the vampire if it succeeds in a " +
            "Perception " + VampireProwlNightEvent.DETECT_VAMPIRE_PERCEPTION_DIFFICULTY + " Reactive Check.\n\n" +
            "If however, the victim does not detect the vampire, the vampire may feed on the victim, " +
            "potentially infecting the victim with vampirism.\n\n" +
            "A vampire can not feed on other vampires.";

    public FeedingOnParty(GameView view) {
        super(view, "Feeding (party)", TEXT);
    }
}
