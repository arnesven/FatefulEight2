package model.mainstory;

import model.Model;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;

import java.awt.*;

public class GainSupportOfPiratesTask extends GainSupportOfRemotePeopleTask {
    public GainSupportOfPiratesTask(Model model) {
        super(WorldBuilder.PIRATE_HAVEN_LOCATION);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("Pirates of the Western Coast") {
            @Override
            public String getText() {
                return "Travel to the Pirate Haven in the western archipelago and gain the support of the pirates.";
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
        return false;
    }
}
