package model.journal;

import model.mainstory.MainStory;
import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.mainstory.VisitLordEvent;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.MainQuest;
import model.quests.Quest;
import model.states.EveningState;
import model.states.dailyaction.TownDailyActionState;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.SpriteQuestMarker;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class StoryPart implements Serializable {
    protected static final Sprite MAP_SPRITE = new SpriteQuestMarker();

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
        MainQuest quest = MainStory.getQuest(questName).copy();
        quest.setStoryPart(this);
        quest.setPortrait(appearance);
        quest.setProvider(portraitName);
        return quest;
    }

    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        return null;
    }

    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) { }

    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) { return new ArrayList<>(); }

    protected abstract boolean isCompleted();

    public String getHexInfo(Point position) {
        return null;
    }

    public void addFactionStrings(List<MyPair<String, String>> result) {

    }

    public EveningState generateEveningState(Model model, boolean freeLodging, boolean freeRations) {
        return null;
    }
}
