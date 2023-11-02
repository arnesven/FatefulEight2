package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import view.MyColors;

public abstract class AuxiliarySpell extends Spell {
    public AuxiliarySpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    protected int getExperience() {
        return 0;
    }

    @Override
    public final boolean castYourself(Model model, GameState state, GameCharacter caster) {
        state.println("");
        boolean preconditionsMet = preCast(model, state, caster);
        if (!preconditionsMet) {
            return false;
        }
        boolean success = super.castYourself(model, state, caster);
        if (success) {
            applyAuxiliaryEffect(model, state, caster);
        }
        return success;
    }

    protected abstract boolean preCast(Model model, GameState state, GameCharacter caster);

    protected abstract void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster);

    public CombatSpell getCombatSpell() { return null; }

    public final boolean canBeCastInCombat() { return getCombatSpell() != null; }

}
