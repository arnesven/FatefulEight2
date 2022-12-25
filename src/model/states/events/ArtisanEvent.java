package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class ArtisanEvent extends DailyEventState {
    public ArtisanEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters an artisan on the road. This particular artisan is a");
        int roll = MyRandom.rollD10();
        List<Item> itemList = new ArrayList<>();
        if (roll <= 2) {
            println(" tailor and offers to sell you some apparel at a discount.");
            itemList.add(model.getItemDeck().getRandomApparel());
        } else if (roll <= 4) {
            println(" smith and offers to sell you a weapon at a discount.");
            itemList.add(model.getItemDeck().getRandomWeapon());
        } else if (roll <= 6) {
            println(" a jeweller and offers to sell you an accessory at a discount.");
            itemList.add(model.getItemDeck().getRandomJewelry());
        } else if (roll <= 8) {
            println(" a cobbler and offers to sell you a pair of shoes at a discount.");
            itemList.add(model.getItemDeck().getRandomShoes());
        } else if (roll <= 10) {
            println(" an enchanter and offers to sell you a wand at a discount.");
            itemList.add(model.getItemDeck().getRandomWand());
        }
        ShopState shop = new ShopState(model, "artisan", itemList,
                new int[]{itemList.get(0).getCost()/2});
        shop.setSellingEnabled(false);
        waitForReturn();
        shop.run(model);
        println("The artisan also offers to educate you in the ways of his trade, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.ART);
        changeClassEvent.areYouInterested(model);
        println("You part ways with the artisan.");
    }
}
