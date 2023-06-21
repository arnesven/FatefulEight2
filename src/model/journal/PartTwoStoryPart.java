package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import view.ScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class PartTwoStoryPart extends StoryPart {
    private final InitialStoryPart initialStoryPart;
    private StoryPart rescueMissionPart;
    private StoryPart deliverPackagePart;

    public PartTwoStoryPart(InitialStoryPart storyPart) {
        this.initialStoryPart = storyPart;
        rescueMissionPart = new RescueMissionStoryPart(initialStoryPart.getCastleName());
        deliverPackagePart = new DeliverPackageStoryPart(initialStoryPart.getWitchPosition());
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.addAll(initialStoryPart.getJournalEntries());
        entries.addAll(rescueMissionPart.getJournalEntries());
        entries.addAll(deliverPackagePart.getJournalEntries());
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        initialStoryPart.handleTownSetup(townDailyActionState);
    }

    @Override
    public void progress(int track) {
        if (track == StoryPart.TRACK_A) {
            rescueMissionPart.progress();
        } else { // track B
            deliverPackagePart.progress();
        }
        if (!initialStoryPart.completed()) {
            initialStoryPart.progress(track);
        }
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        rescueMissionPart.addQuests(model, quests);
        deliverPackagePart.addQuests(model, quests);
    }

    @Override
    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        DailyEventState event = rescueMissionPart.getVisitLordEvent(model, location);
        if (event != null) {
            return event;
        }
        event = deliverPackagePart.getVisitLordEvent(model, location);
        return event;
    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        rescueMissionPart.drawMapObjects(model, x, y, screenX, screenY);
        deliverPackagePart.drawMapObjects(model, x, y, screenX, screenY);
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        List<DailyAction> result = new ArrayList<>();
        result.addAll(rescueMissionPart.getDailyActions(model, worldHex));
        result.addAll(deliverPackagePart.getDailyActions(model, worldHex));
        return result;
    }
}
