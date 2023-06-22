package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.items.potions.Potion;
import model.states.DailyEventState;
import model.states.ShopState;

import java.util.ArrayList;
import java.util.List;

public class WitchHutEvent extends DailyEventState {
    public WitchHutEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.WIT, "Witch");
        println("You find a small hut in a dank grove. Light emanates from " +
                "the window. Inside a witch is stirring a cauldron and " +
                "mumbling strange rhymes.");
        model.getParty().randomPartyMemberSay(model, List.of("Is that an incantation or she just insane?"));
        println("She beckons you inside and offers to sell you a couple of bottles of the draft.");

        List<Item> itemList = new ArrayList<>();
        Potion pot = model.getItemDeck().getRandomPotion();
        itemList.addAll(List.of(pot.copy(), pot.copy(), pot.copy()));
        ShopState shop = new ShopState(model, "witch", itemList,
                new int[]{pot.getCost()/2, pot.getCost()/2, pot.getCost()/2});
        shop.setSellingEnabled(false);
        waitForReturn();
        shop.run(model);
        println("The witch also offers to reveal the dark secrets of witchcraft, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.WIT);
        changeClassEvent.areYouInterested(model);
        println("You leave the witch's hut.");
    }
}
