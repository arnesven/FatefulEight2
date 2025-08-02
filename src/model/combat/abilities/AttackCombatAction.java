package model.combat.abilities;

import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

class AttackCombatAction extends BasicCombatAction {
    public AttackCombatAction(boolean isMelee) {
        super("Attack", true, isMelee);
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        performer.performAttack(model, combat, target);
    }
}
