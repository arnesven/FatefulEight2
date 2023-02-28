package model;

import model.map.UrbanLocation;
import model.tasks.GiveStaffTask;
import model.tasks.SummonTask;
import model.tasks.MissingGlassesTask;

import java.io.Serializable;

public class Summon implements Serializable {
    public static final int ACCEPTED = 0;
    public static final int MET_LORD = 1;
    public static final int COMPLETE = 2;
    private int step = ACCEPTED;

    public SummonTask getTask(Model model, UrbanLocation location) {
        // TODO: Add a bunch more and keep track of which ones have been used.
        //return new MissingGlassesTask(this, model, location);
        return new GiveStaffTask(this, model, location);
    }

    public int getStep() {
        return step;
    }

    public void increaseStep() {
        step++;
    }

}
