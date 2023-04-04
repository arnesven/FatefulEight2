package model;

import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.tasks.CastleDungeonTask;
import model.tasks.GiveStaffTask;
import model.tasks.MissingGlassesTask;
import model.tasks.SummonTask;
import util.MyRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Summon implements Serializable {
    public static final int ACCEPTED = 0;
    public static final int MET_LORD = 1;
    public static final int COMPLETE = 2;
    private int step = ACCEPTED;
    private SummonTask task = null;
    private static Set<String> usedTasks = new HashSet<>();

    private static List<SummonTask> makeAllTasks(Summon summon, Model model, UrbanLocation location) {
        List<SummonTask> tasks = new ArrayList<>();
        tasks.add(new MissingGlassesTask(summon, model, location));
        tasks.add(new GiveStaffTask(summon, model, location));
        // TODO: add more
        return tasks;
    }


    public SummonTask getTask(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation) {
            return new CastleDungeonTask(this, model, location);
        }

        List<SummonTask> allTasks = makeAllTasks(this, model, location);
        if (usedTasks.size() == allTasks.size()) {
            throw new IllegalStateException("All tasks used up!");
        }
        do {
            this.task = MyRandom.sample(allTasks);
        } while (isUsed(this.task));
        use(this.task);
        return this.task;
    }

    private static void use(SummonTask task) {
        usedTasks.add(task.getClass().getCanonicalName());
    }

    private static boolean isUsed(SummonTask task) {
        return usedTasks.contains(task.getClass().getCanonicalName());
    }

    public int getStep() {
        return step;
    }

    public void increaseStep() {
        step++;
    }

}
