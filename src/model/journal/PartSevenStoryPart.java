package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.mainstory.thepast.ThePastJournalEntry;
import model.mainstory.thepast.VisitAncientCityTask;
import model.map.Direction;
import model.map.locations.AncientCityLocation;
import model.quests.MindMachineQuest;
import model.quests.Quest;
import model.states.EveningState;
import model.states.GameState;
import model.states.dailyaction.TownDailyActionState;
import view.JournalView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PartSevenStoryPart extends StoryPart {
    private static final int INITIAL_STEP = 0;
    private static final int TRAVELED_TO_THE_PAST = 1;
    private static final int FOUND_OUT_STUFF = 2;
    private static final int COME_UP_WITH_PLAN = 3;
    private final String castle;
    private int step = INITIAL_STEP;

    public PartSevenStoryPart(Model model, String castle) {
        this.castle = castle;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        JournalEntry mainEntry = new PartSevenJournalEntry();
        return List.of(mainEntry);
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
                    "They're hated by everybody, except for their goons, which they send all over the kingdom to collect taxes and harass people.");
            port.portraitSay(model, state, "And they enforce the Quad's stringent laws. Anybody who commits the smallest transgression is " +
                    "arrested and taken to work in their mines.");
            state.leaderSay("They're in the capital? And where's that?");
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
            model.getParty().addDestinationTask(new VisitAncientCityTask(path.getLast(), cityA));
            path = model.getWorld().generalShortestPath(0, worldHex -> worldHex.getLocation() == cityB);
            model.getParty().addDestinationTask(new VisitAncientCityTask(path.getLast(), cityB));
        }
    }

    @Override
    public EveningState generateEveningState(Model model, boolean freeLodging, boolean freeRations) {
        if (step == FOUND_OUT_STUFF) {
            return new MakeUpAPlanForThePastEventEveningState(model, freeLodging, freeRations);
        }
        return null;
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

    private class MakeUpAPlanForThePastEventEveningState extends EveningState {
        public MakeUpAPlanForThePastEventEveningState(Model model, boolean freeLodging, boolean freeRations) {
            super(model, freeLodging, freeRations, true);
        }

        @Override
        protected void locationSpecificEvening(Model model) {
            if (model.getParty().size() == 1) {
                leaderSay("So... this is the past...");
                println(model.getParty().getLeader().getFirstName() + " looks around.");
                leaderSay("It doesn't look much different than the future actually. But I'd better come up with a plan if I ever want to get back home.");
                leaderSay("Maybe the Quad knows something about time travel? They may not be the friendliest people, but it's the best lead I've got.");
                leaderSay("And I can't let Arabella roam free either. Who knows what temporal paradoxes may occur if I don't get her before she can get to the Quad.");
            } else {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                GameCharacter other2 = other;
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (gc != other && gc != model.getParty().getLeader() &&
                            gc != model.getMainStory().getWillisCharacter() &&
                            gc != model.getMainStory().getCaidCharacter()) {
                        other2 = gc;
                        break;
                    }
                }
                partyMemberSay(other, "So... this is the past...");
                println(other.getFirstName() + " looks around.");
                partyMemberSay(other, "It doesn't look much different than the future actually.");
                leaderSay("Yeah. But we'd better come up with a plan if we ever want to get home.");
                partyMemberSay(other2, "Arabella built the machine that transported us back in time. Maybe she knows how to get us back?");
                leaderSay("I don't think so. It appeared the machine was malfunctioning when that happened. I don't think Arabella ever intended it to do that.");
                partyMemberSay(other, "Maybe the Quad knows something about time travel? Aren't they supposed to be some kind of super powerful mages?");
                leaderSay("Perhaps they do. But by the sound of things, they aren't very friendly.");
                partyMemberSay(other, "But what other leads do we have?");
                leaderSay("None really. We'd better get to the capital then, and I've got a feeling that's where Arabella is headed too.");
                partyMemberSay(other2, "I thought you said she couldn't help us get home.");
                leaderSay("No, I don't think she can. But she's an evil sorceress who worships the Quad. She'll probably try to ingratiate " +
                        "herself with them to learn all their magical secrets.");
                GameCharacter caid = model.getMainStory().getCaidCharacter();
                if (model.getParty().getPartyMembers().contains(caid)) {
                    partyMemberSay(caid, "We don't want that, trust me. Arabella almost was a very dangerous magic user even before she found the Ancient Stronghold. " +
                            "She made a big mess back in my kingdom.");
                    leaderSay("We've got to stop her.");
                } else {
                    partyMemberSay(other, "That would be bad for us.");
                    leaderSay("For everybody, in the past, in the future or anywhere. We've got to stop her.");
                }
                partyMemberSay(other2, "Wait... why should we care what happens here in the past? As long as we can get home, what does it matter what happens to Arabella?");
                leaderSay("We can't rule out that our actions here in the past will affect what happens in the future.");
                partyMemberSay(other2, "What do you mean?");
                GameCharacter willis = model.getMainStory().getWillisCharacter();
                if (model.getParty().getPartyMembers().contains(willis)) {
                    partyMemberSay(willis, "In the book... The Quad was actually overthrown too at some point. " +
                            "That means that those resisting them will actually be successful.");
                } else {
                    leaderSay("Do you remember what Willis told us? The Quad was actually overthrown too at some point. That means that " +
                            "those resisting them will actually be successful.");
                }
                partyMemberSay(other2, "And...?");
                leaderSay("And if we through Arabella into the mix, who knows what's going to happen. Maybe the Quad won't be overthrown.");
                partyMemberSay(other, "Maybe Arabella will manage to usurp the thrown herself.");
                partyMemberSay(other2, "And that changes the future?");
                leaderSay("It could. Or perhaps not. I'm not sure. But we can't take the chance that it doesn't.");
                partyMemberSay(other2, "Alright, you've convinced me.");
            }
            AncientCityLocation capital = getCapitalCity(model);
            Point p = model.getWorld().getPositionForLocation(capital);
            model.getParty().addDestinationTask(new VisitAncientCityTask(p, capital));
            JournalEntry.printJournalUpdateMessage(model);
            progress();
            super.locationSpecificEvening(model);
        }
    }

    private class PartSevenJournalEntry extends ThePastJournalEntry {

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
            if (step == COME_UP_WITH_PLAN) {
                return "You've decided to try to get close to the Quad and potentially meet with them. " +
                        "The Quad's magic seems like your best hope of getting back to your own time.\n\n" +
                        "However, the fact that Arabella roams free and could link up with the Quad worries you. " +
                        "You must find her and stop her.";
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
            if (step == COME_UP_WITH_PLAN) {
                AncientCityLocation cap = getCapitalCity(model);
                return model.getWorld().getPositionForLocation(cap);
            }
            return null;
        }
    }
}
