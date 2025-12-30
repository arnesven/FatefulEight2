package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.Enemy;
import model.mainstory.GainSupportOfJungleTribeTask;
import model.races.Race;
import model.states.GameState;
import util.MyPair;
import util.MyRandom;

import java.util.List;
import java.util.Map;

public class InteractWithJequenEvent extends GeneralInteractionEvent {
    private final GainSupportOfJungleTribeTask task;

    public InteractWithJequenEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model, "Talk to", 5, false);
        this.task = task;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You step into Prince Jequen's hut.");
        showExplicitPortrait(model, task.getJequenPortrait(), "Prince Jequen");
        portraitSay("You have return. Have you found the Jade Crown?");
        leaderSay("No. I wanted to ask you some questions.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("Do you want to ask Jequen to tell the story of his father and grandfather again? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Tell me about your father and grandfather again.");
            portraitSay("Well, to understand the relationship between my father and grandfather, you must understand " +
                    "the state the kingdom was in.");
            leaderSay("You mentioned it was in decline. That it had been for some time?");
            for (MyPair<Boolean, String> pair : MeetWithJequenEvent.makeJequensStory()) {
                if (pair.first) {
                    portraitSay(pair.second);
                } else {
                    leaderSay(pair.second);
                }
            }
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm Prince Jequen. But I usually just go by 'Jequen'. My title doesn't matter without the Jade Crown.";
    }

    @Override
    protected String getRegionReply() {
        return "This is the Southern Kingdom, or what remains of it at least.";
    }

    @Override
    protected String getOutsideOfKingdomNews() {
        return MyRandom.flipCoin() ? "Orcs, lizardmen and frogmen have been very aggressive lately. " +
                "Our warriors are completely preoccupied with fending them off."
                : "Count Aldec's forces have been spotted in the lands to the north. They don't usually come " +
                "this far south. I wonder what is going on.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Prince Jequen", "", Race.SOUTHERN_HUMAN, Classes.None,
                task.getJequenPortrait(), Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH; // Not be used
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                "King Jaq was my grandfather. He hid the Jade Crown from my father. He died of old age a short time after he resigned."),
                "Prince Jaquar", new MyPair<>("Who was Prince Jaquar, and what happened to him?",
                "Prince Jaquar was my father. My grandfather snubbed him of his birthright. Jaquar ventured to one of the " +
                        "pyramids in search of the Jade Crown. He never came back."));
    }
}
