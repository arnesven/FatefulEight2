package model;

import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.tasks.RestoreMansionTask;
import model.tasks.*;
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
    private int summonTask = -1;
    private int completedOnDay = 0;
    private static Set<String> usedTasks = new HashSet<>();

    private static List<SummonTask> makeAllTasks(Summon summon, Model model, UrbanLocation location) {
        List<SummonTask> tasks = new ArrayList<>();
        tasks.add(new MissingGlassesTask(summon, model, location));
        tasks.add(new GiveStaffTask(summon, model, location));
        tasks.add(new GiveMaterialsTask(summon, model, location));
        tasks.add(new GiveIngredientsTask(summon, model, location));
        tasks.add(new GiveFoodTask(summon, model, location));
        tasks.add(new MagicOrbTask(summon, model, location));
        tasks.add(new FencingTask(summon, model, location));
        tasks.add(new DoILookFatTask(summon, model, location));
        tasks.add(new HelpMeWithPuzzleTask(summon, model, location));
        tasks.add(new BecomeASpyForMeTask(summon, model, location));
        tasks.add(new UnlockThisBathroom(summon, model, location));
        tasks.add(new InspireMeForAdventureTask(summon, model, location));
        tasks.add(new ClearOutCultistsHouseTask(summon, model, location));
        tasks.add(new RestoreMansionTask(summon, model, location));
        tasks.add(new ConvinceVampireToLeaveTask(summon, model, location));
        return tasks;
    }


    public SummonTask getTask(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation) {
            return new CastleDungeonTask(this, model, location);
        }
        List<SummonTask> allTasks = makeAllTasks(this, model, location);
        if (summonTask != -1) {
            return allTasks.get(summonTask);
        }
        if (usedTasks.size() == allTasks.size()) {
            throw new IllegalStateException("All tasks used up!");
        }
        SummonTask task;
        do {
            task = MyRandom.sample(allTasks);
        } while (isUsed(task));
        use(task);
        summonTask = allTasks.indexOf(task);
        return task;
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

    public void setCompletedOnDay(int day) {
        this.completedOnDay = day;
    }

    public int getCompletedOnDay() {
        return completedOnDay;
    }
}
