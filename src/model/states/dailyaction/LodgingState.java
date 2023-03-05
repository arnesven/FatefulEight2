package model.states.dailyaction;

import model.Model;
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
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        print("Evening has come. ");
        model.getTutorial().evening(model);
        checkForQuest(model);
        if (freeLodging) {
            println("The party receives food and lodging.");
            model.getParty().lodging(0);
        } else {
            println("The party feasts on what the house has to offer and sleep well in soft beds.");
            model.getParty().lodging(EveningState.lodgingCost(model));
            model.getParty().randomPartyMemberSay(model, List.of("I feel rather refreshed.", "I slept well!3",
                    "Nothing is like a good night's sleep.", "That was a delicious dinner.",
                    "Ahhh, I feel like a person again.", "Can I have a hot bath too?"));
        }
        model.incrementDay();
        return nextState(model);
    }
}
