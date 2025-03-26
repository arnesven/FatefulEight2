package model.states.dailyaction.town;

import model.Model;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.town.GoTheDocksNode;
import model.states.events.TravelByCharteredBoat;

public class CharterBoatAtDocks extends GoTheDocksNode {
    private static final int CHARTER_COST = 35;
    private boolean travelled = false;

    public CharterBoatAtDocks(Model model) {
        super(model);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CharterBoatState(model);
    }

    @Override
    public boolean returnNextState() {
        return travelled;
    }

    private class CharterBoatState extends GameState {
        public CharterBoatState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            println("The captain is willing to let you charter this boat for " + CHARTER_COST + " gold.");
            if (model.getParty().getGold() < CHARTER_COST) {
                println("But you cannot afford it.");
                return model.getCurrentHex().getDailyActionState(model);
            }
            print("Do you charter the boat? (Y/N) ");
            if (yesNoInput()) {
                println("You pay the captain " + CHARTER_COST + " gold to charter the boat.");
                printQuote("Captain", "My ship is yours to command. Where do you want to go?");
                model.getParty().addToGold(-CHARTER_COST);
                travelled = true;
                return new TravelByCharteredBoat(model).run(model);
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
    }
}
