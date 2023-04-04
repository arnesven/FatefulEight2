package model.states.events;

import model.Model;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;

import java.util.ArrayList;
import java.util.List;

public class ArmoryEvent extends DailyEventState {
    public ArmoryEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showSilhouettePortrait(model, "Servant");
        println("One of the Lord's servants approaches you and leads you to the castle armory.");
        portraitSay(model, "I have been instructed to offer you a gift. You may take one of these " +
                "items that fits your fancy.");

        List<Item> items = new ArrayList<>();
        while (items.size() < 4) {
            Item it = model.getItemDeck().getRandomItem();
            if (it.getCost() <= 20) {
                items.add(it);
            }
        }
        ShopState shop = new ShopState(model, "castle armory", items, new int[]{0, 0, 0, 0});
        shop.setMayOnlyBuyOne(true);
        shop.run(model);
    }
}
