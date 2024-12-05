package model.states.events;

import model.Model;
import model.items.puzzletube.DwarvenPuzzleTube;
import model.states.DailyEventState;

import java.awt.*;

public class FindPuzzleTubeEvent extends DailyEventState {
    public FindPuzzleTubeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (alreadyFoundInThisLocation(model)) {
            new NoEventState(model).run(model);
            return;
        }
        setTubeFoundInPosition(model);
        println("The party finds a Dwarven Puzzle Tube!");
        DwarvenPuzzleTube.giveNewTubeToParty(model, model.getParty().getPosition());
    }

    private void setTubeFoundInPosition(Model model) {
        model.getSettings().getMiscFlags().put(makeKeyFromPosition(model.getParty().getPosition()), true);
    }

    public static boolean alreadyFoundInThisLocation(Model model) {
        return model.getSettings().getMiscFlags().containsKey(makeKeyFromPosition( model.getParty().getPosition()));
    }

    private static String makeKeyFromPosition(Point position) {
        return "TUBEFOUNDIN("+position.x + ","+position.y+")";
    }
}
