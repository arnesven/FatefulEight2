package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.items.clothing.HeavyArmorClothing;
import model.items.weapons.AxeWeapon;
import model.items.weapons.BladedWeapon;
import model.items.weapons.BluntWeapon;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class SmithEvent extends DailyEventState {
    public SmithEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean gender = MyRandom.randInt(2) == 0;
        showRandomPortrait(model, Classes.ART, "Smith");
        println("The smith stands in the heat from the furnace. " + heOrSheCap(gender) + " is banging with a mallet on an " +
                "anvil. " + heOrSheCap(gender) + " offers you a unique item.");
        waitForReturn();
        List<Item> items = new ArrayList<>();
        do {
            Item it = model.getItemDeck().getRandomItem(0.98);
            if (it instanceof BladedWeapon || it instanceof BluntWeapon || it instanceof AxeWeapon ||
                    (it instanceof HeavyArmorClothing)) {
                items.add(it);
                break;
            }
        } while (true);

        ShopState shop = new ShopState(model, "Smith", items, new int[]{items.get(0).getCost()/2});
        shop.run(model);
        print("The smith also offers to instruct you in the ways of being an Artisan, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.ART);
        change.areYouInterested(model);
    }
}
