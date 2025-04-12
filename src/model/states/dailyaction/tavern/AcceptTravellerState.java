package model.states.dailyaction.tavern;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.journal.JournalEntry;
import model.states.GameState;
import model.travellers.Traveller;
import model.travellers.TravellerCollection;
import util.MyStrings;

public class AcceptTravellerState extends GameState {
    private final GameState previousState;
    private final Traveller traveller;

    public AcceptTravellerState(Model model, GameState state, Traveller traveller) {
        super(model);
        this.previousState = state;
        this.traveller = traveller;
    }

    @Override
    public GameState run(Model model) {
        if (model.getParty().getActiveTravellers().size() == TravellerCollection.MAX_ACTIVE) {
            println("This " + traveller.getRace().getName() + " traveller is looking for an escort. However " +
                    "you are already escorting " + MyStrings.numberWord(TravellerCollection.MAX_ACTIVE) +
                    " travellers and cannot take on any more.");
        } else {
            println(traveller.getAcceptString());
            print("Do you accept to take the traveller to the destination? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addTraveller(traveller);
                JournalEntry.printJournalUpdateMessage(model);
                traveller.accept(model.getDay());
                traveller.printReady(model, this);
            }
        }
        return previousState;
    }
}
