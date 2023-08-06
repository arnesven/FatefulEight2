package model.states.dailyaction;

import model.Model;
import model.horses.*;
import model.states.DailyActionState;
import model.states.GameState;
import util.MyRandom;
import view.subviews.BuyHorseSubView;
import view.subviews.SubView;

import java.util.List;

public class BuyHorseState extends GameState {
    private static final List<Horse> HORSES = List.of(
            new Pony(), new Pony(), new Pony(),
            new Prancer(), new Regal(), new Sphinx(),
            new Merrygold());

    private Horse horse;

    public BuyHorseState(Model model) {
        super(model);
        this.horse = generateHorse();
    }

    public BuyHorseState(Model model, Horse horse) {
        super(model);
        this.horse = horse;
    }

    @Override
    public GameState run(Model model) {
        println("Bartender: \"We have a nice " + horse.getName() + " for sale for " + horse.getCost() + " gold, if you are interested.\"");
        if (model.getParty().getGold() < horse.getCost()) {
            leaderSay("I'd love to, but I can't afford it right now.");
        } else if (model.getParty().getHorsesFullBlood() + model.getParty().getPonies() >= model.getParty().size() + 2){
            leaderSay("I don't think we can handle more horses right now.");
        } else {
            SubView previous = model.getSubView();
            BuyHorseSubView subView = new BuyHorseSubView(model.getSubView(), this);
            model.setSubView(subView);
            waitForReturn();
            if (subView.didAccept()) {
                println("You bought the " + horse.getName() + ".");
                model.getParty().addToGold(-horse.getCost());
                model.getParty().addHorse(horse);
            } else {
                leaderSay("A fine creature, but unfortunately I have to decline.");
            }
            model.setSubView(previous);
        }
        return new DailyActionState(model);
    }

    public Horse getHorse() {
        return horse;
    }

    public static Horse generateHorse() {
        return MyRandom.sample(HORSES);
    }
}
