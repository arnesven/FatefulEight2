package model.mainstory;

import model.Model;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;

import java.awt.*;

public class GainSupportOfHonorableWarriorsTask extends GainSupportOfRemotePeopleTask {
    private final boolean completed;

    public GainSupportOfHonorableWarriorsTask() {
        super(WorldBuilder.FAR_EASTERN_TOWN_LOCATION);
        this.completed = true;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Honorable Warriors") {
            @Override
            public String getText() {
                return "Gain the support of the Honorable Warriors in the Far Eastern town.";
            }

            @Override
            public boolean isComplete() {
                return GainSupportOfHonorableWarriorsTask.this.isCompleted();
            }

            @Override
            public Point getPosition(Model model) {
                return GainSupportOfHonorableWarriorsTask.this.getPosition();
            }
        };
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
