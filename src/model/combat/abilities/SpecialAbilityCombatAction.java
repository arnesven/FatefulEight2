package model.combat.abilities;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;

public abstract class SpecialAbilityCombatAction extends CombatAction {
    public SpecialAbilityCombatAction(String name, boolean doesFatigue, boolean isMeleeAttack) {
        super(name, doesFatigue, isMeleeAttack);
    }

    public abstract boolean possessesAbility(Model model, GameCharacter performer);

    public boolean canPerformAbility(Model model, GameCharacter performer, Combatant target) {
        return possessesAbility(model, performer) && meetsOtherRequirements(model, performer, target);
    }

    protected abstract boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target);
}
