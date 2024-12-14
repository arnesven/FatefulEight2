package model.states.events;

import model.Model;
import model.items.puzzletube.DwarvenPuzzleTube;
import model.items.puzzletube.FindPuzzleDestinationTask;
import model.states.DailyEventState;
import model.tasks.DestinationTask;
import util.MyLists;

import java.awt.*;

public class FindPuzzleTubeEvent extends DailyEventState {
    public FindPuzzleTubeEvent(Model model) {
        super(model);
    }

    public static boolean alreadyFoundInLocation(Model model, Point position) {
        return model.getSettings().getMiscFlags().containsKey(makeKeyFromPosition(position));
    }

    @Override
    protected void doEvent(Model model) {
        if (alreadyFoundInCurrentLocation(model)) {
            new NoEventState(model).run(model);
            return;
        }
        setTubeFoundInPosition(model);
        println("The party finds a Dwarven Puzzle Tube!");
        DwarvenPuzzleTube.giveNewTubeToParty(model, model.getParty().getPosition());
        DestinationTask task = MyLists.find(model.getParty().getDestinationTasks(), dt ->
                dt instanceof FindPuzzleDestinationTask && dt.getPosition().equals(model.getParty().getPosition()) &&
                        !dt.isCompleted());
        if (task != null) {
            ((FindPuzzleDestinationTask)task).setCompleted(true);
        }
    }

    private void setTubeFoundInPosition(Model model) {
        model.getSettings().getMiscFlags().put(makeKeyFromPosition(model.getParty().getPosition()), true);
    }

    public static boolean alreadyFoundInCurrentLocation(Model model) {
        return alreadyFoundInLocation(model, model.getParty().getPosition());
    }

    private static String makeKeyFromPosition(Point position) {
        return "TUBEFOUNDIN("+position.x + ","+position.y+")";
    }
}
