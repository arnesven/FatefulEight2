package model.mainstory;

import model.Model;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.WorldBuilder;

import java.awt.*;

public class GainSupportOfJungleTribeTask extends GainSupportOfRemotePeopleTask {
    public GainSupportOfJungleTribeTask(Model model) {
        super(WorldBuilder.JUNGLE_PYRAMID_LOCATION);
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new MainStoryTask("The Jungle Tribe") {
            @Override
            public String getText() {
                return "Travel to the southern continent and gain the support of the Jungle Tribe which inhabits that area.";
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
        return true;
    }
}
