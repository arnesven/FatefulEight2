package model.items.potions;

import model.items.Inventory;
import model.items.Item;
import model.items.UsableItem;

public abstract class Potion extends UsableItem {

    public Potion(String name, int cost) {
        super(name, cost);
    }

    static String getPotionPrefixForHigherTier(int tier) {
        switch (tier) {
            case 1:
                return "Greater";
            case 2:
                return "Superior";
            case 3:
                return "Premium";
        }
        return "Extreme";
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getSound() {
        return "bottle";
    }

    @Override
    public int getWeight() {
        return 250;
    }
}
