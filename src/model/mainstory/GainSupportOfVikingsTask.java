package model.mainstory;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;
import model.map.locations.EasternPalaceLocation;
import model.map.locations.VikingVillageLocation;
import model.quests.SavageVikingsQuest;
import model.states.DailyEventState;
import util.MyPair;
import util.MyTriplet;

import java.awt.*;
import java.util.List;

public class GainSupportOfVikingsTask extends GainSupportOfRemotePeopleTask {
    private boolean completed = false;
    private int step = 0;

    public GainSupportOfVikingsTask(Model model) {
        super(WorldBuilder.VIKING_VILLAGE_LOCATION);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Vikings") {
            @Override
            public String getText() {
                return "Gain the support of the Vikings of the North.";
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfVikingsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfVikingsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }


    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        if (model.getCurrentHex().getLocation() instanceof VikingVillageLocation &&
                step == 0) {
            return new MyTriplet<>(SavageVikingsQuest.QUEST_NAME,
                    model.getParty().getLeader().getAppearance(), "Yourself");
        }
        return null;
    }

    @Override
    public void setQuestSuccessful() {
        step++;
    }

    @Override
    public void addFactionString(List<MyPair<String, String>> result) {
        // TODO
    }

    public DailyEventState generateEvent(Model model) {
        return new JustArrivedInVikingTownEvent(model);
    }
}
