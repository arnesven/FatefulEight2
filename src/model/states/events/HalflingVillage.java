package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;

import java.util.List;

public class HalflingVillage extends DailyEventState {
    public HalflingVillage(Model model) {
        super(model);
    }

    @Override
    protected boolean isFreeRations() {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace() == Race.HALFLING) {
                foundVillage(model, gc);
                return;
            }
        }
        print("The party stumbles upon a little miniature village. ");
        println("The halflings quickly race for their dwellings and promptly shut their doors and windows.");
        println("Halfling Woman: \"Go away, we don't want any big-people trouble here!\"");
        model.getParty().randomPartyMemberSay(model, List.of("How rude."));
    }

    private void foundVillage(Model model, GameCharacter halflingCharacter) {
        print("The party stumbles upon a little miniature village. ");
        if (!allHalflings(model.getParty())) {
            println("The halflings quickly race for their dwellings.");
            model.getParty().partyMemberSay(model, halflingCharacter, List.of("It's alright, these people are with me!"));
        } else {
            println("The halflings welcome you as if you were related to them.");
        }
        EveningState.buyRations(model, this);
        new HalflingEvent(model).doEvent(model);
    }

    private boolean allHalflings(Party party) {
        for (GameCharacter gc : party.getPartyMembers()) {
            if (gc.getRace() != Race.HALFLING) {
                return false;
            }
        }
        return true;
    }
}
