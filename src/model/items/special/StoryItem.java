package model.items.special;

import model.items.Inventory;
import model.items.Item;

public abstract class StoryItem extends Item {
    public StoryItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getSound() {
        return null;
    }
}
