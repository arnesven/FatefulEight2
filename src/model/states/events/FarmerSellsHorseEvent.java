package model.states.events;

import model.Model;
import model.states.DailyEventState;
import model.states.dailyaction.BuyHorseState;
import util.MyRandom;

public class FarmerSellsHorseEvent extends DailyEventState {
    public FarmerSellsHorseEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("You meet a poor farmer who is selling a horse.");
        BuyHorseState buyHorse = new BuyHorseState(model, "Farmer");
        buyHorse.setPrice(MyRandom.randInt(8, 20));
        buyHorse.run(model);
        if (model.getParty().getHorseHandler().getAvailableHorse(model) == null) {
            model.getParty().getHorseHandler().generateNextAvailableHorse();
        }
    }
}
