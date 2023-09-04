package model.items.potions;

import model.items.Inventory;
import model.items.Item;
import model.items.UsableItem;

public abstract class Potion extends UsableItem {

    public Potion(String name, int cost) {
        super(name, cost);
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getSound() {
        return "bottle";
    }
}
