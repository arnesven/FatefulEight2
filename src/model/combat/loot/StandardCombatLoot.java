package model.combat.loot;

import model.Model;
import model.Party;
import model.items.Item;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class StandardCombatLoot extends CombatLoot {

    private int gold = 0;
    private List<Item> items = new ArrayList<>();

    public StandardCombatLoot(Model model, int bonus) {
        int dieRoll = MyRandom.randInt(10) + bonus;
        if (dieRoll <= 7) {
            gold = dieRoll - 1;
        } else if (dieRoll <= 9) {
            items.add(model.getItemDeck().getRandomItem(0.10));
        }
        if (dieRoll == 7) {
            gold++;
        }
        if (dieRoll >= 10) {
            items.add(model.getItemDeck().getRandomItem(0.5));
        }
    }

    public StandardCombatLoot(Model model) {
        this(model, 0);
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        if (items.size() > 0) {
            bldr.append(items.get(0).getName());
        }
        for (int i = 1; i < items.size(); ++i) {
            bldr.append("\n" + items.get(i).getName());
        }
        return bldr.toString();
    }

    @Override
    protected void specificGiveYourself(Party party) {
        for (Item it : items) {
            party.getInventory().addItem(it);
        }
    }

    @Override
    public int getGold() {
        return gold;
    }

    protected List<Item> getItems() {
        return items;
    }

    protected void setGold(int g) {
        gold = g;
    }
}
