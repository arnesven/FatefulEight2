package model.combat.abilities;

import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

class PassCombatAction extends BasicCombatAction {
    public PassCombatAction() {
        super("Pass", false, false);
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println("");
    }
}
