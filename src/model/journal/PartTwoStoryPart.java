package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;

import java.util.ArrayList;
import java.util.List;

public class PartTwoStoryPart extends StoryPart {
    private StoryPart rescueMissionPart;
    private StoryPart deliverPackagePart;

    public PartTwoStoryPart(Model model) {
        throw new IllegalStateException("Should not be instantiated.");
        //deliverPackagePart = new WitchStoryPart(model.getMainStory().getWitchPosition(), model.getMainStory().getCastleName());
        //rescueMissionPart = new RescueMissionStoryPart(deliverPackagePart, model.getMainStory().getCastleName(), model.getMainStory().getLibraryTownName());

    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        List<JournalEntry> entries = new ArrayList<>();
        entries.addAll(rescueMissionPart.getJournalEntries());
        entries.addAll(deliverPackagePart.getJournalEntries());
        return entries;
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) { }

    @Override
    public void progress() {
        throw new IllegalStateException("Should not be called!");
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
    protected StoryPart getNextStoryPart(Model model, int track) {
        return new PartThreeStoryPart(model.getMainStory().getCastleName(), model.getMainStory().getLibraryTownName());
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

    @Override
    protected boolean isCompleted() {
        return rescueMissionPart.isCompleted() && deliverPackagePart.isCompleted();
    }

    public boolean witchPartCompleted() {
        return deliverPackagePart.isCompleted();
    }
}
