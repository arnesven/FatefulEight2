package model.states.events;

import model.Model;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;

import java.util.ArrayList;
import java.util.List;

public class MerchantEvent extends DailyEventState {
    private final boolean withIntro;

    public MerchantEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public MerchantEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("A large wagon with tons of wares stacked upon it, and beside it stands a plump character in fancy clothing.");
            println("\"Please, I have lots of merchandise and I just know you adventurer types are always in need of something.");
            println("Won't you please have a look?\"");
            waitForReturn();
        }
        List<Item> items = new ArrayList<>();
        items.addAll(model.getItemDeck().draw(6));
        ShopState merchantShop = new ShopState(model, "merchant", items,
                new int[]{items.get(0).getCost()/2, items.get(1).getCost()/2,
                items.get(2).getCost()-2, items.get(3).getCost()-2,
                items.get(4).getCost()+10, items.get(5).getCost()+10});
        merchantShop.run(model);
    }
}
