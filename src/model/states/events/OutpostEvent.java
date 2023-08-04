package model.states.events;

import model.Model;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.dailyaction.LodgingState;

import java.util.List;

public class OutpostEvent extends DailyEventState {
    private boolean stayingAtInn = false;

    public OutpostEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("I see some chimney smoke over there."));
        println("It appears to be a little hut. It's too small to be called an inn, but the owner is offering a warm " +
                "meal and a cot. ");
        if (model.getParty().getGold() > 0) {
            print("If you pay 1 gold per party member you can stay and rest here for the night. Do you wish to stay at the outpost? (Y/N) ");
            if (yesNoInput()) {
                if (model.getParty().getGold() < model.getParty().size()) {
                    println("The proprietor scoffs as you short him.");
                    model.getParty().addToGold(-model.getParty().getGold());
                } else {
                    model.getParty().addToGold(-model.getParty().size());
                }
                stayingAtInn = true;
            }
        } else {
            println("You explain that you do not have any money. The proprietor seems to " +
                    "take pity on you and lets you stay at the outpost for free this time.");
            stayingAtInn = true;
        }
    }

    @Override
    protected GameState getEveningState(Model model) {
        if (!stayingAtInn) {
            return super.getEveningState(model);
        }
        return new LodgingState(model, true);
    }
}
