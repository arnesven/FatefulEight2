package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;

import java.util.ArrayList;
import java.util.List;

public class HeadquartersEveningState extends EveningState {
    public HeadquartersEveningState(Model model) {
        super(model);
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        if (model.getSpellHandler().creatureComfortsCastToday(model)) {
            println("The party receives food and lodging for free.");
            model.getParty().lodging(0);
        } else if (hasEnoughFood(model)) {
            int foodLoss = Math.min(model.getParty().size(), model.getParty().getFood());
            model.getParty().getHeadquarters().addToFood(foodLoss - model.getParty().size());
            if (foodLoss < model.getParty().size()) {
                println("You do not have enough rations on hand, so you cover the difference with rations from headquarters.");
            }
            println("The party has consumes rations but sleep soundly in comfortable beds.");
            model.getParty().addToFood(-foodLoss);
            model.getParty().lodging(0);
            if (model.getParty().size() > 1) {
                model.getParty().randomPartyMemberSay(model, List.of(
                        "Nice to sleep in my own bed.",
                        "Nice to not sleep in a tent for once.",
                        "Ah, peace and quiet.",
                        "I need some rest.",
                        "Let's hit the sack people.",
                        "Nighty-night everyone",
                        "Tomorrow's another day.",
                        "I'm about to fall asleep.",
                        "Bedtime. At least for me.",
                        "These rations are a bit stale.",
                        "It's been an alright day I suppose.",
                        "Yaaawn!", "Good night everybody."));
            }
        } else {
            print("There is a bed for everyone but not enough rations for. ");
            List<GameCharacter> remaining = new ArrayList<>(model.getParty().getPartyMembers());
            while (model.getParty().getFood() > 0) {
                print("Please select who gets to eat: ");
                GameCharacter gc = model.getParty().partyMemberInput(model, this, remaining.get(0));
                if (remaining.contains(gc)) {
                    println(gc.getFirstName() + " consumes rations.");
                    model.getParty().addToFood(-1);
                    gc.addToHP(2);
                    if (!gc.hasCondition(VampirismCondition.class)) {
                        gc.addToSP(1);
                    }
                    remaining.remove(gc);
                } else {
                    println("That party member has already consumed rations this evening.");
                }
            }
            starveAndKill(model, remaining);
        }
    }

    protected boolean hasEnoughFood(Model model) {
        return model.getParty().getFood() + model.getParty().getHeadquarters().getFood() >= model.getParty().size();
    }
}
