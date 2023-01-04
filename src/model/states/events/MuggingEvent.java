package model.states.events;

import model.Model;
import model.enemies.MuggerEnemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class MuggingEvent extends DailyEventState {
    public MuggingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Two scruffy men approach the party as you cut through" +
                " an alley.");
        println("Thug: \"Okay kid, hand it over!\"");
        model.getParty().randomPartyMemberSay(model, List.of("Are we just gonna let these bozos take our stuff?"));
        print("Fight the muggers? (Y/N)");
        if (yesNoInput()) {
            runCombat(List.of(new MuggerEnemy('A'), new MuggerEnemy('A')));
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Here, take this and leave us alone."));
            int foodTaken = MyRandom.randInt(model.getParty().getFood());
            int goldTaken = MyRandom.randInt(model.getParty().getGold());
            println("The party hands over " + foodTaken + " rations and " + goldTaken + " gold.");
            model.getParty().addToFood(-foodTaken);
            model.getParty().addToGold(-goldTaken);
        }
    }
}
