package model.journal;

import model.MainStory;
import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.MainQuest;
import model.quests.Quest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class StoryPart implements Serializable {

    public abstract List<JournalEntry> getJournalEntries();

    public abstract void handleTownSetup(TownDailyActionState townDailyActionState);

    public final void increaseStep(Model model) {
        progress();
        JournalEntry.printJournalUpdateMessage(model);
    }

    public abstract void progress();

    public final void transitionStep(Model model, int track) {
        model.getMainStory().addStoryPart(getNextStoryPart(model, track));
    }

    public final void transitionStep(Model model) {
        transitionStep(model, 0);
    }

    protected abstract StoryPart getNextStoryPart(Model model, int track);

    public abstract void addQuests(Model model, List<Quest> quests);

    protected MainQuest getQuestAndSetPortrait(String questName, CharacterAppearance appearance, String portraitName) {
        MainQuest quest = MainStory.getQuest(questName);
        quest.setStoryPart(this);
        quest.setPortrait(appearance);
        quest.setProvider(portraitName);
        return quest;
    }

    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        return null;
    }

    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) { }

    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) { return new ArrayList<>(); }

    protected abstract boolean isCompleted();
}
