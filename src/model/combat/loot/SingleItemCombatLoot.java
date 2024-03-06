package model.combat.loot;

import model.Party;
import model.items.Item;

public class SingleItemCombatLoot extends CombatLoot {
    private final Item item;

    public SingleItemCombatLoot(Item item) {
        this.item = item;
    }

    @Override
    public String getText() {
        return item.getName();
    }

    @Override
    protected void specificGiveYourself(Party party) {
        party.getInventory().addItem(item);
    }
}
