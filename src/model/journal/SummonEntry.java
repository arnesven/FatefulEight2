package model.journal;

import model.Model;
import model.Summon;
import model.map.HexLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.tasks.SummonTask;

import java.awt.*;

public class SummonEntry implements JournalEntry {
    private final UrbanLocation urb;
    private final Summon summon;
    private final boolean complete;
    private SummonTask task;

    public SummonEntry(Model model, UrbanLocation urb, Summon summon) {
        this.urb = urb;
        this.summon = summon;
        if (summon.getStep() > Summon.ACCEPTED) {
            this.task = summon.getTask(model, urb);
        }
        this.complete = summon.getStep() == Summon.COMPLETE;
    }

    @Override
    public boolean isTask() {
        return true;
    }

    @Override
    public Point getPosition(Model model) {
        return model.getWorld().getPositionForLocation((HexLocation) urb);
    }

    @Override
    public String getName() {
        String title = urb.getLordTitle().substring(0, 1).toUpperCase() +
                urb.getLordTitle().substring(1);
        if (urb instanceof TownLocation) {
            return title + " of " + ((TownLocation) urb).getTownName();
        }
        return title + " of " + urb.getPlaceName();
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        if (summon.getStep() == Summon.ACCEPTED) {
            bldr.append("The " + urb.getLordTitle() + " of " + urb.getPlaceName() +
                    " has requested your presence.\n\n");
            bldr.append((urb.getLordGender() ? "She" : "He") + " asks that you visit " +
                    (urb.getLordGender() ? "her" : "him") + " at the ");
            if (urb instanceof TownLocation) {
                bldr.append("town hall of ");
            }
            bldr.append(urb.getPlaceName());
            bldr.append(".\n\n");
            bldr.append("You have no additional information.");
        } else {
            bldr.append("We have met with the " + urb.getLordTitle() + " " + urb.getLordName() + " of " + urb.getPlaceName() + "\n\n");
            bldr.append((urb.getLordGender() ? "She" : "He") + " wants us to help " +
                    (urb.getLordGender() ? "her" : "him") + " with a special problem;\n\n");
            bldr.append(task.getJournalDescription());

            if (summon.getStep() == Summon.COMPLETE) {
                bldr.append("\n\nYou completed this task on day " + summon.getCompletedOnDay() + ".");
            }
        }

        return bldr.toString();
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isFailed() {
        return false;
    }
}
