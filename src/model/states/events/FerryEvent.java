package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class FerryEvent extends RiverEvent {
    private boolean paid;

    public FerryEvent(Model model) {
        super(model, true);
        paid = false;
    }

    @Override
    public String getDistantDescription() {
        return "a riverbank with a ferry";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to ferry", "There's a ferry just down the river from here");
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return !paid;
    }

    @Override
    protected void doRiverEvent(Model model) {
        showSilhouettePortrait(model, "Ferryman");
        print("There's a ferry here. The ferryman will take the party across the river for 10 gold.");
        if (model.getParty().getGold() < 10) {
            println(" But you cannot afford that and the ferryman does not seem interested in haggling.");
        } else {
            print(" Do you accept (Y/N)? ");
            if (yesNoInput()) {
                paid = true;
                model.getParty().addToGold(-10);
                model.getParty().randomPartyMemberSay(model, List.of(
                        "A nice little boat ride.",
                        "Take us across ferryman.",
                        "And we don't even have to get our feet wet."));
            } else {
                model.getParty().randomPartyMemberSay(model, List.of("I'm sure we'll find somewhere else to cross."));
            }
        }
    }

}
