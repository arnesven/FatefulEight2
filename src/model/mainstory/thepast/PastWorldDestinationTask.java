package model.mainstory.thepast;

import model.Model;
import model.journal.JournalEntry;
import model.map.WorldType;
import model.tasks.DestinationTask;

import java.awt.*;

public abstract class PastWorldDestinationTask extends DestinationTask {

    public PastWorldDestinationTask(Point position, String description) {
        super(position, description);
    }

    @Override
    public WorldType getWorld() {
        return WorldType.thePast;
    }
}
