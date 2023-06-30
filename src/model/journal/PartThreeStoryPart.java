package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.quests.FrogmenProblemQuest;
import model.quests.Quest;
import model.quests.TroubleInTheLibraryQuest;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.*;
import util.MyRandom;
import view.MyColors;
import view.SimpleMessageView;
import view.sprites.LibrarySprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PartThreeStoryPart extends StoryPart {

    private static final Sprite SPRITE = new LibrarySprite();

    private final StoryPart previousStoryPart;
    private static final int INITIAL_STEP = 0;
    private static final int TALK_TO_WILLIS_STEP = 1;
    private static final int DO_QUEST_STEP = 2;
    private static final int QUEST_COMPLETED_STEP = 3;
    private final String castleName;
    private final String libraryTown;

    private int internalStep = INITIAL_STEP;

    public PartThreeStoryPart(StoryPart previous, String castleName, String libraryTown) {
        this.previousStoryPart = previous;
        this.castleName = castleName;
        this.libraryTown = libraryTown;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.add(new LibraryTask());
        entries.addAll(previousStoryPart.getJournalEntries());
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        if (!previousStoryPart.isCompleted()) {
            previousStoryPart.handleTownSetup(townDailyActionState);
        } else {
            if (townDailyActionState.getTown().getName().equals(libraryTown)) {
                int randomSeed = townDailyActionState.getTown().getName().hashCode();
                townDailyActionState.addNodeInFreeSlot(new VisitLibraryNode(townDailyActionState.getTown(), this), randomSeed);
            }
        }
    }

    @Override
    public void progress(int track) {
        if (!previousStoryPart.isCompleted()) {
            previousStoryPart.progress(track);
            if (previousStoryPart.isCompleted()) {
                internalStep++;
            }
        } else {
            internalStep++;
        }
    }

    @Override
    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        if (!previousStoryPart.isCompleted()) {
            return previousStoryPart.getVisitLordEvent(model, location);
        }
        return super.getVisitLordEvent(model, location);
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (!previousStoryPart.isCompleted()) {
            previousStoryPart.addQuests(model, quests);
        } else {
            if (internalStep == DO_QUEST_STEP) {
                if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof TownLocation) {
                    TownLocation loc = (TownLocation) model.getCurrentHex().getLocation();
                    if (loc.getName().equals(libraryTown)) {
                        GameCharacter willis =  model.getMainStory().getWillisCharacter();
                        quests.add(getQuestAndSetPortrait(TroubleInTheLibraryQuest.QUEST_NAME, willis.getAppearance(),
                                willis.getFirstName()));
                    }
                }
            }
        }
    }



    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    @Override
    protected boolean isCompleted() {
        return internalStep > QUEST_COMPLETED_STEP;
    }

    private class LibraryTask extends MainStoryTask {
        public LibraryTask() {
            super("Go to the Library");
        }

        @Override
        public String getText() {
            switch (internalStep) {
                case INITIAL_STEP:
                    return "Inform the lord of " + castleName + " about the Crimson Pearl.";
                case TALK_TO_WILLIS_STEP:
                    return "Go to the library in the " + libraryTown + ".";
                case DO_QUEST_STEP:
                    return "Complete the quest 'Trouble in the Library'.";
                case QUEST_COMPLETED_STEP:
                    return "Talk to Willis again.";
                default:
                    return "You helped Willis shut down the automatons in the library.\n\nCompleted.";
            }
        }

        @Override
        public boolean isComplete() {
            return PartThreeStoryPart.this.isCompleted();
        }
    }

    private class VisitLibraryNode extends DailyActionNode {
        public VisitLibraryNode(TownLocation town, PartThreeStoryPart partThreeStoryPart) {
            super("Visit Library");
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            return new VisitLibraryEvent(model, model.getWorld().getCastleByName(castleName));
        }

        @Override
        public Sprite getBackgroundSprite() {
            return SPRITE;
        }

        public void drawYourself(Model model, Point p) {
            model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            if (internalStep == INITIAL_STEP) {
                model.getLog().addAnimated("The library isn't open right now.");
            }
            return internalStep > INITIAL_STEP;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
    }

    private class VisitLibraryEvent extends DailyEventState {
        private final CastleLocation castle;

        public VisitLibraryEvent(Model model, CastleLocation castle) {
            super(model);
            this.castle = castle;
        }

        @Override
        protected boolean isFreeRations() {
            return true;
        }

        @Override
        protected boolean isFreeLodging() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            showWillis(model);
            if (internalStep == INITIAL_STEP) {
                // should not happen
            } else if (internalStep == TALK_TO_WILLIS_STEP) {
                portraitSay("Oh hi there. No, you can't go into the library right now.");
                leaderSay("But we have important business. On authority of the lord of the realm, we are here to " +
                        "speak to the historian Willis Johanssen.");
                portraitSay("Yes, that's me. But you still can't go in.");
                leaderSay("Why not?");
                portraitSay("Uhm, there's some... let's say trouble inside.");
                leaderSay("Haven't cleaned in a while?");
                portraitSay("Oh, heavens no. But it's not that. Let's go to my house and speak more about this important business.");
                println("The party accompanies Willis to her home in town.");
                portraitSay("Ah... peace and quite. Now please, what's this all about?");
                leaderSay("We have been sent by " + castle.getLordTitle() + " " + castle.getLordName() + " to inquire about matters " +
                        "regarding the security of the realm.");
                portraitSay("Oh goodness!");
                leaderSay("We would like to learn all there is to know about the Quad and their Crimson Pearls.");
                portraitSay("Hmm... the Crimson Pearls. Enchanted pearls with which one could dominate another " +
                        "person's mind? I think that's just an old folk tale?");
                println("You pull the crimson pearl out of your pocket and place it on the table.");
                leaderSay("Perhaps. But we've found this pearl, and we believe somebody was using it to control a tribe of frogmen.");
                portraitSay("Interesting...");
                println("Willis picks up the pearl and inspects it.");
                portraitSay("Well I know part of the story. The 'Quad' were...");
                leaderSay("A group of sorcerers who performed a coup with the help of the pearls' magic.");
                portraitSay("Ah... I see you know that part as well.");
                leaderSay("We were told you would have more answers for us. Like what happened to the Quad or their magic.");
                portraitSay("I'm afraid I don't know the answer. But I am sure there are several thick tomes on the subject in the library. " +
                        "I always took them for fiction and didn't pay them much attention, but now I may have to take a closer look at them.");
                leaderSay("So can we go to the library now? What's the 'trouble'?");
                portraitSay("Well, you see, being a historian is great. I love my profession, but I've been one for a long time. " +
                        "And a little while back I had a revelation.");
                List<String> otherProfessions = List.of("cage fighter", "master chef", "lion tamer", "lumberjack", "tanner", "royal jester");
                if (model.getParty().size() > 1) {
                    partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                            "You wanted to become a " + MyRandom.sample(otherProfessions) + " instead?");
                }
                portraitSay("Hehehe, no... I took up enchanting.");
                leaderSay("It's always good to have a hobby.");
                portraitSay("Yes, and I've gotten rather good at enchanting too. My speciality is magical machinery, it's called 'Arcanism'.");
                leaderSay("Never heard of it.");
                portraitSay("It's a rather new school of magic.");
                leaderSay("What can you do with it?");
                portraitSay("I've enchanted bookcases so that they can move books around by themselves. Very handy when you're short on staff.");
                if (model.getParty().size() > 1) {
                    partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                            "There's a shortage of people who want to work at the library? That's surprising...");
                }
                portraitSay("My staircases are also enchanted. They can easily be moved around inside the library.");
                leaderSay("Please get to the point...");
                portraitSay("Alright. My latest project is my most ambitious one yet. " +
                        "I wanted to have actual mechanical servants who could perform a variety of tasks. I've been working on a machine that sends out " +
                        "arcanistic waves which control mechanical automatons in the library. I finally got it working a few days ago, but...");
                leaderSay("Let me guess. It didn't go as planned?");
                portraitSay("Not quite. I expected the automatons to carry books, sweep the floors, repair broken windows, " +
                        "you know that sort of stuff. But I've must have gotten one of the incantations wrong, because as soon as I " +
                        "activated the machine, the automatons just attacked me!");
                leaderSay("Machines rising up and attacking their master. How original...");
                portraitSay("I've tried to get back into the library to shut down the machine, but I can't seem to get past those hunks of metal. " +
                        "Now I fear I won't be able to get back to my books, and god knows what they're doing to the place while I'm away.");
                leaderSay("Calm down. We'll take care of the situation.");
                portraitSay("You will? Really?");
                leaderSay("Of course. We have to. The answers we seek are in the library remember? And you're going to help us " +
                        "find them once we take care of your crazy experiment, right?");
                portraitSay("Of course! But please, prepare well, those automatons are very dangerous. And there's a fair few of them in there.");
                leaderSay("We can handle ourselves.");
                model.getMainStory().increaseStep(model);
                model.transitionToDialog(new SimpleMessageView(model.getView(),
                        "Warning. It is recommended that your party members " +
                                "are at least level 3 before doing the quest " +
                                "'Trouble in the Library'."));
            }
        }

        private void showWillis(Model model) {
            showExplicitPortrait(model, model.getMainStory().getWillisCharacter().getAppearance(), "Willis");
        }
    }
}
