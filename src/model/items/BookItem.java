package model.items;

import model.Model;
import model.characters.GameCharacter;

public abstract class BookItem extends UsableItem {

    public BookItem(String name, int cost) {
        super(name, cost);
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getSound() {
        return "paper";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return model.getParty().getPartyMembers().contains(target);
    }

    @Override
    public String getUsageVerb() {
        return "Read";
    }
}
