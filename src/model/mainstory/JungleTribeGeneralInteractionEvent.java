package model.mainstory;

import model.Model;
import model.TimeOfDay;
import model.enemies.Enemy;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;

import java.util.List;

public abstract class JungleTribeGeneralInteractionEvent extends GeneralInteractionEvent {

    public JungleTribeGeneralInteractionEvent(Model model, int stealMoney) {
        super(model, "Talk to", stealMoney, false);
    }

    @Override
    protected GameState doEndOfEventHook(Model model) {
        GameState toReturn = super.doEndOfEventHook(model);
        if (MyRandom.rollD6() > 2) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
            println("There is still time to do other things today.");
        }
        return toReturn;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH; // unused
    }

    @Override
    protected String getRegionReply() {
        return "This is the Southern Kingdom, the land of the Jungle Tribe.";
    }

    @Override
    protected String getOutsideOfKingdomNews() {
        return MyRandom.flipCoin() ? "Orcs, lizardmen and frogmen have been very aggressive lately. " +
                "Our warriors are completely preoccupied with fending them off."
                : "Northerners in armor have been spotted in the lands to the north. They don't usually come " +
                "this far south. I wonder what is going on.";
    }
}
