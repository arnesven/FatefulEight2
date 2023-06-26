package model.journal;

import model.Model;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class PartThreeStoryPart extends StoryPart {
    private final StoryPart previousStoryPart;

    public PartThreeStoryPart(StoryPart previous) {
        this.previousStoryPart = previous;
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
        previousStoryPart.progress(track);
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

    private static class LibraryTask extends MainStoryTask {
        public LibraryTask() {
            super("Go to Library");
        }

        @Override
        public String getText() {
            return "Go to the library in...";
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    }
}
