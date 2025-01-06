package model.states.events;

import model.Model;
import model.items.Item;
import model.items.potions.BeerPotion;
import model.items.potions.FineWine;
import model.items.potions.WinePotion;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class BeverageTentEvent extends DailyEventState {
    public BeverageTentEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You step into the beverage tent. There are plenty of tables and a rather large bar. The " +
                "proprietor seems to be serving mostly drinks, some alcoholic, and only a lighter selection of snacks.");
        model.getLog().waitForAnimationToFinish();
        List<Item> itemsForSale = new ArrayList<>();
        for (int i = MyRandom.randInt(4, 6); i > 0; --i) {
            itemsForSale.add(MyRandom.flipCoin() ? new WinePotion() : new BeerPotion());
            if (MyRandom.randInt(20) == 0) {
                itemsForSale.add(new FineWine());
            }
        }
        ShopState state = new ShopState(model, "Beverage Tent", itemsForSale);
        state.setSellingEnabled(false);
        state.run(model);
    }

}
