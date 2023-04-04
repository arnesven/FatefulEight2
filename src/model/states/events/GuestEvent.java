package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class GuestEvent extends DailyEventState {
    public GuestEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.FARMER, "Farmers");
        println("The party is invited into the home of these farmers who " +
                "gracefully offer a warm meal and the prospect of sleeping in " +
                "the barn for the night.");
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }
}
