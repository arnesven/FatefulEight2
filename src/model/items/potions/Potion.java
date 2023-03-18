package model.items.potions;

import model.Inventory;
import model.Model;
import model.combat.Combatant;
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
