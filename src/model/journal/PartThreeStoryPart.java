package model.journal;

import model.Model;
import model.map.UrbanLocation;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class PartThreeStoryPart extends StoryPart {
    private final StoryPart previousStoryPart;
    private static final int INITIAL_STEP = 0;
    private static final int DO_QUEST_STEP = 1;
    private static final int QUEST_COMPLETED_STEP = 2;
    private static final int ASK_EVERIX_STEP = 3;
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
        previousStoryPart.handleTownSetup(townDailyActionState);
    }

    @Override
    public void progress(int track) {
        if (!previousStoryPart.isCompleted()) {
            previousStoryPart.progress(track);
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
        previousStoryPart.addQuests(model, quests);
    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class LibraryTask extends MainStoryTask {
        public LibraryTask() {
            super("Go to the Library");
        }

        @Override
        public String getText() {
            if (internalStep == INITIAL_STEP) {
                return "Inform the lord of " + castleName + " about the Crimson Pearl.";
            }
            return "Go to the library in " + libraryTown + ".";
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    }
}
