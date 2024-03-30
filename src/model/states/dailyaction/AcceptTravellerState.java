package model.states.dailyaction;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.states.GameState;
import model.travellers.Traveller;

public class AcceptTravellerState extends GameState {
    private final AdvancedDailyActionState previousState;
    private final Traveller traveller;

    public AcceptTravellerState(Model model, AdvancedDailyActionState state, Traveller traveller) {
        super(model);
        this.previousState = state;
        this.traveller = traveller;
    }

    @Override
    public GameState run(Model model) {
        println(traveller.getAcceptString());
        print("Do you accept to take the traveller to the destination? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addTraveller(traveller);
        }
        return previousState;
    }
}
