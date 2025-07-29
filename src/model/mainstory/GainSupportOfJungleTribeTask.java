package model.mainstory;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;
import model.quests.Quest;
import model.races.Race;
import util.MyPair;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfJungleTribeTask extends GainSupportOfRemotePeopleTask {
    private boolean completed = false;

    public GainSupportOfJungleTribeTask(Model model) {
        super("Jungle Tribe", WorldBuilder.JUNGLE_PYRAMID_LOCATION);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Jungle Tribe") {
            @Override
            public String getText() {
                return "Travel to the southern continent and gain the support of the Jungle Tribe which inhabits that area." +
                        (completed ? "\n\nCompleted" : "");
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfJungleTribeTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfJungleTribeTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        return null;
    }

    @Override
    public void setQuestSuccessful() {
        this.completed = true;
    }

    @Override
    public void addFactionString(List<MyPair<String, String>> result) {
        // TODO
    }

    @Override
    public void setCompleted() {
        this.completed = true;
    }

    @Override
    public boolean supportsFromTheSea() {
        return false;
    }

    @Override
    public CharacterAppearance getLeaderPortrait() {
        return PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.SOUTHERN_HUMAN); // TODO: Fix
    }

    @Override
    public String getLeaderName() {
        return "TODO";
    }
}
