package model.states.dailyaction;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.combat.PoisonCondition;
import model.states.EveningState;
import model.states.GameState;

import java.util.List;

public class LodgingState extends EveningState {
    private final boolean freeLodging;

    public LodgingState(Model model, boolean freeLodging) {
        super(model);
        this.freeLodging = freeLodging;
    }

    @Override
    protected boolean showTentSubView() {
        return false;
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        if (freeLodging) {
            println("The party has received food and lodging.");
            model.getParty().lodging(0);
        } else {
            println("The party feasts on what the house has to offer and sleep well in soft beds.");
            model.getParty().lodging(EveningState.lodgingCost(model));
            model.getParty().randomPartyMemberSay(model, List.of("I feel rather refreshed.", "I slept well!3",
                    "Nothing is like a good night's sleep.", "That was a delicious dinner.",
                    "Ahhh, I feel like a person again.", "Can I have a hot bath too?"));
        }
        removePoison(model.getParty());
    }

    private void removePoison(Party party) {
        for (GameCharacter gc : party.getPartyMembers()) {
            if (gc.hasCondition(PoisonCondition.class)) {
                gc.removeCondition(PoisonCondition.class);
                println(gc.getName() + " is no longer poisoned.");
            }
        }
    }
}
