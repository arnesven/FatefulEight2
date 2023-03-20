package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class SalvageEvent extends DailyEventState {
    private final String text;
    private final int amount;

    public SalvageEvent(Model model, String text, int amount) {
        super(model);
        this.text = text;
        this.amount = amount;
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters a" + text + ". It seems to be hastily abandoned and there are some crates " +
                "and package scattered about.");
        model.getParty().randomPartyMemberSay(model, List.of("I wonder what happened here.", "What a mess.",
                "Ambushed by raiders?", "Maybe this was a merchant caravan?"));
        model.getParty().randomPartyMemberSay(model, List.of("Nobody is around. Why don't we see if there is anything to salvage?"));
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 7);
        if (success) {
            println("You thoroughly search the wreckage and are happy to find that it isn't all trash.");
            int dieRoll = MyRandom.rollD10();
            if (dieRoll < 4) {
                model.getParty().addToFood(5);
                println("The party finds 5 rations.");
            } else if (dieRoll < 7) {
                model.getParty().getInventory().addToIngredients(5);
                println("The party finds 5 ingredients.");
            } else if (dieRoll < 10) {
                model.getParty().getInventory().addToMaterials(5);
                println("The party finds 5 materials.");
            } else {
                println("The party finds a chest.");
                new ChestEvent(model).findAChest(model);
            }
        }
    }
}
