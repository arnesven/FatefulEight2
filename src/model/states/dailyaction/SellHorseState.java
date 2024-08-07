package model.states.dailyaction;

import model.Model;
import model.horses.Horse;
import model.states.DailyActionState;
import model.states.GameState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class SellHorseState extends GameState {
    private final String buyer;

    public SellHorseState(Model model, String buyer) {
        super(model);
        this.buyer = buyer;
    }

    @Override
    public GameState run(Model model) {
        buyerSay("Which horse did you want to sell?");
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
                printQuote("Bartender", "We'll take care of " + himOrHer(horse.getGender()) +
                        " from now on then.");
            }
        }
        return new DailyActionState(model);
    }

    protected void buyerSay(String text) {
        printQuote(buyer, text);
    }
}
