package model.states;

import model.Model;

import java.util.List;

public class TempleEveningState extends EveningState {
    public TempleEveningState(Model model) {
        super(model, false, false);
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        print("Please select how you will spend the evening.");
        if (model.getParty().getGold() >= model.getParty().size()) {
            int res = multipleOptionArrowMenu(model, 24, 35, List.of("Camp outside the temple", "Stay at temple (1 gold)"));
            if (res == 1) {
                println("The party receives a basic meal from the temple kitchens, then spends the night in the temple barracks.");
                model.getParty().lodging(1);
            } else {
                notLodging(model);
            }
        } else {
            println("You cannot afford the food and lodging at the temple. ");
            notLodging(model);
        }
    }
}