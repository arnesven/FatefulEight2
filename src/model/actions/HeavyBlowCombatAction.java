package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public class HeavyBlowCombatAction extends StaminaCombatAbility {
    public static final int LABOR_RANKS_REQUIREMENT = 3;

    public HeavyBlowCombatAction() {
        super("Heavy Blow");
    }

    @Override
    public void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().heavyBlow(model);
        combat.println(performer.getFirstName() + " does a powerful swing!");
        performer.doOneAttack(combat, target, false, 2, 10);
    }
}
