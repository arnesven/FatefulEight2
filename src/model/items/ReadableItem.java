package model.items;

import model.Model;
import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public abstract class ReadableItem extends UsableItem {

    public ReadableItem(String name, int cost) {
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
