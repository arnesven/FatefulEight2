package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.ArrayList;

public class KitchenDutyEvent extends DailyEventState {
    public KitchenDutyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.None, "Monk");
        portraitSay("Yes the food and lodging is cheap, but if you could lend " +
                "a hand, we would be grateful.");
        println("How can you refuse this humble monk? However, slaving away in the kitchen is " +
                "no easy task and the party members are completely wiped at the end of the day.");
        randomSayIfPersonality(PersonalityTrait.snobby, new ArrayList<>(), "This is servants' stuff...");
        println("Each party member exhausts one SP");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToSP(-1);
        }
        model.getParty().addToGold(model.getParty().size());
        println("The party receives " + model.getParty().size() + " gold.");
    }
}
