package model.states.dailyaction;

import model.Model;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.GameState;
import util.MyRandom;
import view.subviews.TradeHorsesSubView;

import java.util.ArrayList;
import java.util.List;

public class TradeHorsesState extends GameState {
    private static final String STABLE_MASTER_NAME = "Stable Master";
    private final List<Horse> availableHorses;
    private boolean isInBuyState;

    public TradeHorsesState(Model model, List<Horse> availableHorses) {
        super(model);
        this.availableHorses = availableHorses;
        this.isInBuyState = true;
    }

    @Override
    public GameState run(Model model) {
        println("You walk up to the stable and meet the " + STABLE_MASTER_NAME + ".");
        TradeHorsesSubView subView = new TradeHorsesSubView(availableHorses);
        model.setSubView(subView);

        do {
            waitForReturnSilently();
            if (subView.getTopIndex() == 2) {
                break;
            } else if (subView.getTopIndex() == 0 && !isInBuyState && !availableHorses.isEmpty()) {
                goToBuyState(model, subView);
            } else if (subView.getTopIndex() == 1 && isInBuyState && model.getParty().hasHorses()) {
                goToSellState(model, subView);
            } else if (subView.getTopIndex() == -1) { // Not in top row.
                if (isInBuyState) {
                    buyAHorse(model, subView);
                    if (availableHorses.isEmpty()) {
                        if (!model.getParty().hasHorses()) {
                            break; // Nothing more to do
                        } else {
                            goToSellState(model, subView);
                        }
                    }
                } else {
                    sellAHorse(model, subView);
                    if (!model.getParty().hasHorses()) {
                        if (availableHorses.isEmpty()) {
                            break; // Nothing more to do
                        } else {
                            goToBuyState(model, subView);
                        }
                    }
                }
            }
        } while (true);

        return model.getCurrentHex().getDailyActionState(model);
    }

    private void goToSellState(Model model, TradeHorsesSubView subView) {
        subView.setHorses(model.getParty().getHorseHandler());
        subView.setInSell();
        isInBuyState = false;
    }

    private void goToBuyState(Model model, TradeHorsesSubView subView) {
        subView.setHorses(availableHorses);
        subView.setInBuy();
        isInBuyState = true;
    }

    private void sellAHorse(Model model, TradeHorsesSubView subView) {
        Horse horse = subView.getSelectedHorse();
        printQuote(STABLE_MASTER_NAME, "Oh, you want to sell this " + horse.getName() + "? I can " +
                "take " + himOrHer(horse.getGender()) + " off your hands. How does " + horse.getCost()/2 + " gold sound?");
        print("Are you sure you want to sell the " + horse.getName() + "? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().getHorseHandler().sellHorse(model, horse);
            subView.removeHoses(horse);
        }
    }

    private void buyAHorse(Model model, TradeHorsesSubView subView) {
        if (!model.getParty().canBuyMoreHorses()) {
            println("You can't handle more horses right now.");
            return;
        }

        Horse horse = subView.getSelectedHorse();

        String rest = " You can have " + himOrHer(horse.getGender()) + " for " + horse.getCost() + " gold.";
        printQuote(STABLE_MASTER_NAME, MyRandom.sample(List.of("Oh the " + horse.getName() + ". A fine creature.",
                "Interested in the " + horse.getName() + "?", "Yes, the " + horse.getName() + "...",
                "This is a well-fed " + horse.getName() + ".")) + rest);
        if (horse.getCost() > model.getParty().getGold()) {
            leaderSay("Unfortunately, I can't afford it right now.");
            return;
        }

        print("Are you sure you want to buy the " + horse.getName() + "? (Y/N) ");
        if (yesNoInput()) {
            printQuote(STABLE_MASTER_NAME,
                    MyRandom.sample(List.of("A wise choice.", "Good doing business with you.",
                            "Now you are the owner of this fine " + horse.getName() + ".")));
            availableHorses.remove(horse);
            subView.removeHoses(horse);
            model.getParty().spendGold(horse.getCost());
            model.getParty().getHorseHandler().addHorse(horse);
        } else {
            printQuote(STABLE_MASTER_NAME,
                    MyRandom.sample(List.of("Well, I don't mind keeping this " + horse.getName() + ".",
                    "Just tell me if you change your mind.", "A shame, it's a fine animal.")));
        }
    }
}
