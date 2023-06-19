package model.journal;

import model.Model;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.io.Serializable;
import java.util.List;

public abstract class StoryPart implements Serializable {

    public abstract List<JournalEntry> getJournalEntries();

    public abstract void handleTownSetup(TownDailyActionState townDailyActionState);

    public abstract void progress(Model model);

    public abstract void addQuests(Model model, List<Quest> quests);

    public abstract StoryPart transition(Model model);
}
