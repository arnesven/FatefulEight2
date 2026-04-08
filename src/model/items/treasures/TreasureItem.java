package model.items.treasures;

import model.items.Inventory;
import model.items.Item;

public abstract class TreasureItem extends Item {

    public TreasureItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        return "Treasure";
    }


    @Override
    public String getSound() {
        return "metal-ringing";
    }
}
