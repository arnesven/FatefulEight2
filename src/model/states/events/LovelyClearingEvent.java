package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class LovelyClearingEvent extends DailyEventState {
    public LovelyClearingEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a clearing";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Clearing", "The party comes to a lovely clearing in the woods. Many beautiful flowers grow here. " +
                "It's an excellent spot to collect some potion ingredients.");
        model.getParty().getInventory().addToIngredients(10);
        println("The party gains 10 ingredients.");
        model.getParty().randomPartyMemberSay(model, List.of("These may come in handy."));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find clearing",
                "There's a clearing not far from here. All kinds of marvelous plants grow there");
    }
}
