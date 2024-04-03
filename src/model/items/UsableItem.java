package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;

public abstract class UsableItem extends Item {
    public UsableItem(String name, int cost) {
        super(name, cost);
    }

    public abstract String useYourself(Model model, GameCharacter gc);

    public abstract boolean canBeUsedOn(Model model, GameCharacter target);

    @Override
    public boolean canBeUsedFromMenu() {
        return true;
    }

    public String getUsageVerb() {
        return "Use";
    }

    public boolean removeAfterUse() {
        return true;
    }
}
