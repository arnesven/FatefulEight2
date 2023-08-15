package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import model.states.dailyaction.BuyHorseState;
import util.MyRandom;

import java.util.List;

public class MarketEvent extends DailyEventState {
    public MarketEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Today is market day. The party casually browses the stands and booths.");
        int roll = MyRandom.rollD10();
        if (roll <= 2) {
            new FoodStandsEvent(model).doEvent(model);
        } else if (roll <= 5) {
            horseTrader(model);
        } else {
            pushyMerchant(model);
        }
    }

    private void horseTrader(Model model) {
        println("A breeder is displaying some horses.");
        BuyHorseState buyHorse = new BuyHorseState(model, "Horse Trader");
        buyHorse.setPrice(model.getParty().getHorseHandler().getAvailableHorse(model).getCost() +
                MyRandom.randInt(-10, 5));
        buyHorse.run(model);
    }

    private void pushyMerchant(Model model) {
        showRandomPortrait(model, Classes.MERCHANT, "Pushy Merchant");
        portraitSay("Hey you! You gotta 'ave a look at my stuff. Premium quality and damn fine prices!");
        model.getParty().randomPartyMemberSay(model, List.of("Doesn't hurt to look.",
                "I'm sure it's the same junk as usual, but let's look.",
                "He looks very honest... why don't we see what he has for sale?"));
        waitForReturn();
        List<Item> items = model.getItemDeck().draw(12);
        int[] prices = new int[12];
        for (int i = 0; i < 4; ++i) {
            prices[i] = items.get(i).getCost() / 2;
            prices[i + 4] = items.get(i + 4).getCost();
            prices[i + 8] = items.get(i + 8).getCost() + 10;
        }
        ShopState shop = new ShopState(model, "Pushy Merchant", items, prices);
        shop.setSellingEnabled(false);
        shop.run(model);
    }
}
