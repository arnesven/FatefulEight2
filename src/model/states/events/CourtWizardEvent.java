package model.states.events;

import model.Model;
import model.classes.Classes;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CourtWizardEvent extends DailyEventState {
    public CourtWizardEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The court wizard has a few spells for sale.");
        println("Court Wizard: \"I'm sorry if my prices aren't competitive. I don't have time to " +
                "check the market value of spell books.\"");
        List<Item> items = new ArrayList<>();
        items.add(model.getItemDeck().getRandomWand());
        items.add(model.getItemDeck().getRandomPotion());
        items.add(model.getItemDeck().getRandomPotion());
        int noOfSpells = MyRandom.randInt(3, 6);
        for (int i = 0; i < noOfSpells; ++i) {
            items.add(model.getItemDeck().getRandomSpell());
        }
        int[] costs = new int[items.size()];
        for (int i = 0; i < items.size() - 3; ++i) {
            costs[i] = items.get(i).getCost() + MyRandom.randInt(-6, 6);
        }
        ShopState shop = new ShopState(model, "Court Wizard", items, costs);
        shop.run(model);
        println("The Court Wizard also offers to train you in the ways of being a wizard, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIZ);
        change.areYouInterested(model);
    }
}