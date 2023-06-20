package model.journal;

import model.Model;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class DeliverPackageStoryPart extends StoryPart {

    private int internalStep = 0;

    @Override
    public List<JournalEntry> getJournalEntries() {
        if (internalStep > 0) {
            return List.of(new FindTheWitch());
        }
        return new ArrayList<>();
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress(int track) {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    public static class FindTheWitch extends MainStoryTask {
        public FindTheWitch() {
            super("Find the Witch");
        }

        @Override
        public String getText() {
            return "Find Everix's acquaintance, the witch, to ask about the crimson pearl.";
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    }
}
