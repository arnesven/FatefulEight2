package model.journal;

import model.Model;
import model.map.Direction;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.map.locations.AncientCityLocation;
import model.quests.MindMachineQuest;
import model.quests.Quest;
import model.states.GameState;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.GeneralInteractionEvent;
import view.JournalView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class PartSevenStoryPart extends StoryPart {
    private static final int INITIAL_STEP = 0;
    private static final int TRAVELED_TO_THE_PAST = 1;
    private static final int FOUND_OUT_STUFF = 2;
    private final String castle;
    private int step = INITIAL_STEP;

    public PartSevenStoryPart(Model model, String castle) {
        this.castle = castle;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new JournalEntry() {
            @Override
            public String getName() {
                return "The Prophecy of the Quad";
            }

            @Override
            public String getText() {
                if (step == INITIAL_STEP) {
                    return "Complete the quest " + MindMachineQuest.QUEST_NAME + ".";
                }
                if (step == FOUND_OUT_STUFF) {
                    return "You have been transported back in time to the time when the " +
                            "Quad took control of the ancient kingdom of Recca.\n\n" +
                            "You need to figure out what to do next.";
                }
                return "In an attempt to stop Arabella's plans you tried to destroy her Mind Machine. " +
                        "The machine malfunctioned and transported you to an unknown location.\n\n" +
                        "Now you need to figure out where you are.";
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return null;
            }
        });
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {
        step++;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    public void askAboutRegion(Model model, GameState state, PortraitSubView port) {
        port.portraitSay(model, state, "This is the kingdom of Recca.");
        if (step == TRAVELED_TO_THE_PAST) {
            state.leaderSay("'Recca', that sounds familiar... Wait a minute. What year is it?");
            port.portraitSay(model, state, "Year? Uhm, I think it's 2425, or is it 26 now?");
            model.getParty().randomPartyMemberSay(model, List.of("Good golly. We've been transported back in time!"));
            port.portraitSay(model, state, "Back in time? Whatever do you mean?");
            state.leaderSay("Never mind that. This is the kingdom of Recca? Which is ruled by an evil king?");
            port.portraitSay(model, state, "King Maximus, yes, or it was. Until the Quadrificus took over. " +
                    "People thought things would get better now... If only we had known.");
            state.leaderSay("The Quadrificus? Must be the Quad. Incredible... What do you know about the Quadrificus?");
            port.portraitSay(model, state, "They appeared in this kingdom a few years ago. Powerful mages it is said that they are. And they must be, " +
                    "King Maximus ruled this Recca with an iron fist, but somehow they overthrew him.");
            state.leaderSay("Where are they?");
            port.portraitSay(model, state, "They're holed up in the capital city and nobody can get close to them. " +
                    "They're hated by everybody, except for their goons, which they send all over the kingdom to collect taxes, harass people and " +
                    "enforce the Quad's stringent laws.");
            state.leaderSay("The capital? And where's that?");
            AncientCityLocation capital = getCapitalCity(model);
            model.getWorld().dijkstrasByLand(model.getParty().getPosition());
            List<Point> path = model.getWorld().generalShortestPath(0, worldHex -> worldHex.getLocation() == capital);
            port.portraitSay(model, state, "The " + capital.getName() + ", it's " +
                    Direction.getLongNameForPath(model.getParty().getPosition(), path) + " of here. " +
                    "But it's quite the journey from here.");
            state.leaderSay("Thank you. We'll be on our way.");
            port.portraitSay(model, state, "Just a minute. The thing you were saying before, about going back in time.");
            state.leaderSay("Ahh... well.. don't worry about that.");
            port.portraitSay(model, state, "Well, the thing is. Just a few hours ago, a woman came by and asked the same types of questions. " +
                    "I'm positive she also said something about 'time'. Quite the coincidence, don't you think?");
            state.leaderSay("A woman? Was it a " + model.getMainStory().getArabellaAppearance().getRace().getName().toLowerCase() + "? Tall, and fair?");
            port.portraitSay(model, state, "You know her?");
            state.leaderSay("Arabella! So she must be here too...");
            state.println(model.getParty().getLeader().getFirstName() + " smacks " + GameState.hisOrHer(model.getParty().getLeader().getGender()) +
                    " fist into " + GameState.hisOrHer(model.getParty().getLeader().getGender()) + " palm.");
            port.portraitSay(model, state, "You know, if you want to go to a city, I would not recommend the capital. " +
                    "I went there a few months back and barely made it out alive! If the Quadrificous' goons won't get you, bandits or monsters will!");
            state.leaderSay("That does sound bad...");
            AncientCityLocation cityA = getCityA(model);
            AncientCityLocation cityB = getCityB(model);
            port.portraitSay(model, state, "You should visit the " + cityA.getName() + " or the " + cityB.getName() + ". Those places are much safer. The " +
                    "Quadrificus doesn't have the same hold there, and I've heard there's something of a resistance movement too.");
            state.leaderSay("People are resisting the Quadrificus?");
            path = model.getWorld().generalShortestPath(0, worldHex -> worldHex.getLocation() == cityA);
            port.portraitSay(model, state, "That's what I've heard. Head to the " + cityA.getName() + ", it's the closest. It is " +
                    Direction.getLongNameForPath(model.getParty().getPosition(), path) + " of here.");
            state.leaderSay("This information has been very valuable. Thank you very much.");
            port.portraitSay(model, state, "Safe travels friend.");
            progress();
            JournalEntry.printJournalUpdateMessage(model);
        }
    }

    private static AncientCityLocation internalGetCity(Model model, String cityName) {
        return (AncientCityLocation) model.getWorld().getUrbanLocationByPlaceName("the City of " + cityName);
    }

    public static AncientCityLocation getCapitalCity(Model model) {
        return internalGetCity(model, model.getMainStory().getPastCapitalCity());
    }

    public static AncientCityLocation getCityA(Model model) {
        return internalGetCity(model, model.getMainStory().getPastCityA());
    }

    public static AncientCityLocation getCityB(Model model) {
        return internalGetCity(model, model.getMainStory().getPastCapityB());
    }
}
