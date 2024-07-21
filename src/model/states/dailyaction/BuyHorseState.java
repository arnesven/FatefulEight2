package model.states.dailyaction;

import model.Model;
import model.horses.*;
import model.states.DailyActionState;
import model.states.GameState;
import view.subviews.BuyHorseSubView;
import view.subviews.SubView;


public class BuyHorseState extends GameState {

    private final String seller;
    private int price;

    public BuyHorseState(Model model, String seller) {
        super(model);
        this.price = model.getParty().getHorseHandler().getAvailableHorse(model).getCost();
        this.seller = seller;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public GameState run(Model model) {
        Horse horse = model.getParty().getHorseHandler().getAvailableHorse(model);
        sellerSay(seller, "We have a nice " + horse.getName() + " for sale for " + price + " gold, if you are interested.");
        model.getTutorial().horses(model);
        if (model.getParty().getGold() < price) {
            leaderSay("I'd love to, but I can't afford it right now.");
        } else if (!model.getParty().canBuyMoreHorses()){
            leaderSay("I don't think we can handle more horses right now.");
        } else {
            SubView previous = model.getSubView();
            BuyHorseSubView subView = new BuyHorseSubView(model.getSubView(), price);
            model.setSubView(subView);
            waitForReturn();
            if (subView.didAccept()) {
                println("You bought the " + horse.getName() + ".");
                model.getParty().getHorseHandler().buyAvailableHorse(model, price);
            } else {
                leaderSay("A fine creature, but unfortunately I have to decline.");
            }
            model.setSubView(previous);
        }
        return new DailyActionState(model);
    }

    protected void sellerSay(String seller, String text) {
        printQuote(seller, text);
    }

}
