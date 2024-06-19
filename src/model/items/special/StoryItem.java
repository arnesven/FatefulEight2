package model.items.special;

import model.Model;
import model.items.Inventory;
import model.items.Item;
import model.journal.JournalEntry;

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
