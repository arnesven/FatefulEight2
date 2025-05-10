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
    public GainSupportOfRemotePeopleTask(Point position) {
        super(position, "");
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
}
