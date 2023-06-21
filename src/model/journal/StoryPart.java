package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import view.ScreenHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class StoryPart implements Serializable {

    public static final int TRACK_A = 0;
    public static final int TRACK_B = 1;
    public static final int TRACK_C = 2;

    public abstract List<JournalEntry> getJournalEntries();

    public abstract void handleTownSetup(TownDailyActionState townDailyActionState);

    public abstract void progress(int track);

    public void progress() {
        progress(StoryPart.TRACK_A);
    }

    public abstract void addQuests(Model model, List<Quest> quests);

    public abstract StoryPart transition(Model model);

    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        return null;
    }

    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) { }

    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) { return new ArrayList<>(); }
}
