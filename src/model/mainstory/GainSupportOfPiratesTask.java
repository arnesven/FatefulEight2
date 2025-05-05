package model.mainstory;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.MainStorySpawnWest;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;
import model.map.locations.PirateHavenLocation;
import model.quests.AvertTheMutinyQuest;
import model.quests.Quest;
import util.MyTriplet;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class GainSupportOfPiratesTask extends GainSupportOfRemotePeopleTask {
    public static final String CAPTAIN_NAME = "Blackbone";
    private final AdvancedAppearance blackboneAppearance;
    private boolean blackboneMet = false;
    private boolean completed = false;

    public GainSupportOfPiratesTask(Model model) {
        super(WorldBuilder.PIRATE_HAVEN_LOCATION);
        this.blackboneAppearance = PortraitSubView.makeRandomPortrait(Classes.PIRATE_CAPTAIN);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("Pirates of the Western Coast") {
            @Override
            public String getText() {
                if (blackboneMet) {
                    return "Complete the quest '" + AvertTheMutinyQuest.QUEST_NAME + "'.";
                }
                return "Travel to the Pirate Haven in the western archipelago and gain the support of the pirates." +
                        (completed ? "\n\nCompleted" : "");
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfPiratesTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfPiratesTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public CharacterAppearance getCaptainAppearance() {
        return blackboneAppearance;
    }

    public void setBlackboneMet(boolean blackboneMet) {
        this.blackboneMet = blackboneMet;
    }

    @Override
    public MyTriplet<String, CharacterAppearance, String> addQuests(Model model) {
        if (model.getCurrentHex().getLocation() instanceof PirateHavenLocation &&
                blackboneMet && !isCompleted()) {
            return new MyTriplet<>(AvertTheMutinyQuest.QUEST_NAME, blackboneAppearance, "Captain " + CAPTAIN_NAME);
        }
        return null;
    }

    @Override
    public void setQuestSuccessful() {
        this.completed = true;
    }
}
