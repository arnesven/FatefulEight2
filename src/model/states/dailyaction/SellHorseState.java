package model.states.dailyaction;

import model.Model;
import model.horses.Horse;
import model.states.DailyActionState;
import model.states.GameState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class SellHorseState extends GameState {
    public SellHorseState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("Bartender: \"Which horse did you want to sell?\"");
        List<String> options = new ArrayList<>();
        for (Horse h : model.getParty().getHorseHandler()) {
            options.add(h.getName());
        }
        options.add("CANCEL");
        int selected = multipleOptionArrowMenu(model, 32, 18, options);
        if (selected < model.getParty().getHorseHandler().size()) {
            Horse horse = model.getParty().getHorseHandler().get(selected);
            print("Are you sure you want to sell the " + horse.getName() + " for " + horse.getCost()/2 + " gold? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().getHorseHandler().sellHorse(model, horse);
                println("Bartender: \"We'll take care of " + hisOrHer(MyRandom.flipCoin()) + " from now on then.\"");
            }
        }
        return new DailyActionState(model);
    }
}
