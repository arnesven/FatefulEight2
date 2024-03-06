package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public abstract class PassiveCombatAction extends CombatAction {
    public PassiveCombatAction(String name) {
        super(name, false, false);
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        throw new IllegalStateException("doAction of PassiveCombatAction should never be called!");
    }
}
