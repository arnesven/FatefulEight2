package model.mainstory;

import model.Model;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;

import java.awt.*;

public class GainSupportOfVikingsTask extends GainSupportOfRemotePeopleTask {
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
        return true;
    }
}
