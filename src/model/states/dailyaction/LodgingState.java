package model.states.dailyaction;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.combat.conditions.PoisonCondition;
import model.states.EveningState;

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
                    "Ahhh, I feel like a person again.", "I got to sleep in a real bed for once.",
                    "Can I have a hot bath too?", "It cost a little, but it was worth it.",
                    "Good food, decent rooms.", "This was a pleasant place. Let's stay here again some time."));
        }
        removePoison(model.getParty());
    }

    public void removePoison(Party party) {
        for (GameCharacter gc : party.getPartyMembers()) {
            if (gc.hasCondition(PoisonCondition.class)) {
                gc.removeCondition(PoisonCondition.class);
                println(gc.getName() + " is no longer poisoned.");
            }
        }
    }
}
