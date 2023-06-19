package model.journal;

import model.Model;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class PartTwoStoryPart extends StoryPart {
    private final InitialStoryPart initialStoryPart;
    private int internalStep = 0;

    public PartTwoStoryPart(InitialStoryPart storyPart) {
        this.initialStoryPart = storyPart;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.addAll(initialStoryPart.getJournalEntries());
        entries.add(new GoToCastleTask(initialStoryPart.getCastleName()));
        if (internalStep > 0) {
            entries.add(new FindTheWitch());
        }
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        initialStoryPart.handleTownSetup(townDailyActionState);
    }

    @Override
    public void progress(Model model) {
        internalStep++;
        if (!initialStoryPart.completed()) {
            initialStoryPart.progress(model);
        }
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }
}
