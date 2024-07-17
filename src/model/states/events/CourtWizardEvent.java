package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourtWizardEvent extends DailyEventState {
    public CourtWizardEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit court wizard", "There's a court wizard who usually has some spell books for sale");
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.WIZ, "Court Wizard");
        println("The court wizard has a few spells for sale.");
        portraitSay("I'm sorry if my prices aren't competitive. I don't have time to " +
                "check the market value of spell books.");
        randomSayIfPersonality(PersonalityTrait.stingy, new ArrayList<>(),
                "You should. We won't buy anything overpriced because of your laziness.");
        List<Item> items = new ArrayList<>();
        items.add(model.getItemDeck().getRandomWand());
        items.add(model.getItemDeck().getRandomPotion());
        items.add(model.getItemDeck().getRandomPotion());
        int noOfSpells = MyRandom.randInt(3, 6);
        for (int i = 0; i < noOfSpells; ++i) {
            items.add(model.getItemDeck().getRandomSpell());
        }
        Collections.shuffle(items);
        int[] costs = new int[items.size()];
        for (int i = 0; i < items.size() - 3; ++i) {
            costs[i] = items.get(i).getCost() + MyRandom.randInt(-6, 6);
            if (costs[i] < 0) {
                costs[i] = items.get(i).getCost() - 1;
            }
        }
        for (int i = items.size() - 3; i < items.size(); ++i) {
            costs[i] = items.get(i).getCost();
        }
        ShopState shop = new ShopState(model, "Court Wizard", items, costs);
        shop.run(model);
        println("The Court Wizard also offers to train you in the ways of being a wizard, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.WIZ);
        change.areYouInterested(model);
    }
}
