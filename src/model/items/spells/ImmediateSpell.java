package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import view.MyColors;

public abstract class ImmediateSpell extends AuxiliarySpell {

    public ImmediateSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    public String tryCastSpell(Model model, GameCharacter gc) {
        if (model.isInCombat()) {
            return "You cannot cast " + getName() + " right now.";
        }
        model.getSpellHandler().acceptSpell(getName());
        return super.tryCastSpell(model, gc);
    }

    @Override
    public void triggerInterrupt(GameCharacter caster) {
        // Do not throw exception
    }
}
