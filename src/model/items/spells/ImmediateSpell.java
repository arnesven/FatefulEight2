package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import view.MyColors;

public abstract class ImmediateSpell extends AuxiliarySpell {

    public ImmediateSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        if (model.isInCombat()) {
            return "You cannot cast " + getName() + " right now.";
        }
        model.getSpellHandler().acceptSpell(getName());
        model.getSpellHandler().tryCast(this, gc);
        return gc.getFirstName() + " is casting " + getName() + "...";
    }

    @Override
    public void triggerInterrupt(GameCharacter caster) {
        // Do not throw exception
    }
}
