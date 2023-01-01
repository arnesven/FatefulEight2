package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;

import java.util.List;

public class WitchHutEvent extends DailyEventState {
    public WitchHutEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A small hut in a dank grove. Light emanates from " +
                "the window. Inside a witch is stirring a cauldron and " +
                "mumbling strange rhymes.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().randomPartyMemberSay(model, List.of("Is that an incantation or she just insane?"));
        println("She beckons you inside and offers to sell you a bottle of the draft.");

        List<Item> itemList = List.of(model.getItemDeck().getRandomPotion());
        ShopState shop = new ShopState(model, "witch", itemList,
                new int[]{5});
        shop.setSellingEnabled(false);
        waitForReturn();
        shop.run(model);
        println("The witch also offers to reveal the dark secrets of witchcraft, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.WIT);
        changeClassEvent.areYouInterested(model);
        println("You leave the witch's hut.");
    }
}
