package model.journal;

import model.Model;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class PartFourStoryPart extends StoryPart {
    private final String castleName;

    public PartFourStoryPart(String castleName) {
        this.castleName = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.add(new InformLordEntry());
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() { }

    @Override
    public void addQuests(Model model, List<Quest> quests) { }

    @Override
    public StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class InformLordEntry extends MainStoryTask {
        public InformLordEntry() {
            super("Return to " + castleName);
        }

        @Override
        public String getText() {
            return "Inform the lord of " + castleName + " about what you have learned about the Quad.";
        }

        @Override
        public boolean isComplete() {
            return PartFourStoryPart.this.isCompleted();
        }
    }
}
