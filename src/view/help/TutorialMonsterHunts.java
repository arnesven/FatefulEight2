package view.help;

import model.tasks.MonsterHuntDestinationTask;
import view.GameView;

public class TutorialMonsterHunts extends HelpDialog {
    private static final String TEXT = "Monster Hunts can provide a sizable income for your party.\n\n" +
            "When a town is unable to deal with a monster threat themselves, the lord of that town may contract " +
            "monster hunters to deal with the trouble.\n\n" +
            "Monster hunts will require the party to travel to a hex near the town. Once there, the party must " +
            "succeed in a Survival " + MonsterHuntDestinationTask.TRACK_DIFFICULTY + " Skill Check to track down " +
            "the monster. The party can keep trying to track the monster and kill it each day. " +
            "Once the party manages to kill the monster the reward can be claimed by returning " +
            "to the town and visiting town hall.";

    public TutorialMonsterHunts(GameView view) {
        super(view, "Monster Hunts", TEXT);
    }
}
