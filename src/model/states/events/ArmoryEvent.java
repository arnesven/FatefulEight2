package model.states.events;

import model.Model;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import model.states.TransferItemState;

import java.util.ArrayList;
import java.util.List;

public class ArmoryEvent extends DailyEventState {
    public ArmoryEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit armory", "There's an armory. Usually the lord's servant let's you take an item for free");
    }

    @Override
    protected void doEvent(Model model) {
        showSilhouettePortrait(model, "Servant");
        println("One of the Lord's servants approaches you and leads you to the castle armory.");
        portraitSay("I have been instructed to offer you a gift. You may take one of these " +
                "items that fits your fancy.");

        List<Item> items = new ArrayList<>();
        while (items.size() < 4) {
            Item it = model.getItemDeck().getRandomItem();
            if (it.getCost() <= 20) {
                items.add(it);
            }
        }
        ShopState shop = new TransferItemState(model, "castle armory", items);
        shop.setMayOnlyBuyOne(true);
        shop.run(model);
    }
}
