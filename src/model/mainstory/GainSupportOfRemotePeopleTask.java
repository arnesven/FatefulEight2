package model.mainstory;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.journal.JournalEntry;
import model.quests.Quest;
import model.tasks.DestinationTask;
import util.MyPair;
import util.MyTriplet;

import java.awt.*;
import java.util.List;

public abstract class GainSupportOfRemotePeopleTask extends DestinationTask {
    private final String name;

    public GainSupportOfRemotePeopleTask(String name, Point position) {
        super(position, "");
        this.name = name;
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return null;
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return false;
    }

    public abstract MyTriplet<String, CharacterAppearance, String> addQuests(Model model);

    public abstract void setQuestSuccessful();

    public abstract void addFactionString(List<MyPair<String, String>> result);

    public abstract void setCompleted(); // For testing

    public abstract boolean supportsFromTheSea();

    public String getRemotePeopleName() {
        return name;
    }

    public abstract CharacterAppearance getLeaderPortrait();

    public abstract String getLeaderName();
}
