package model.combat.abilities;

import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

class DelayCombatAction extends BasicCombatAction {
    public DelayCombatAction() {
        super("Delay", false, false);
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.delayCombatant(performer);
    }
}
