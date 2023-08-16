package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.states.CombatEvent;

public abstract class StaminaCombatAbility extends CombatAction {
    private boolean takeAnotherAction = false;

    public StaminaCombatAbility(String name) {
        super(name, true);
    }

    @Override
    public boolean takeAnotherAction() {
        return takeAnotherAction;
    }

    @Override
    public final void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.getSP() > 0) {
            performer.addToSP(-1);
            combat.print(performer.getFirstName() + " exhausts 1 Stamina Point. ");
            doStaminaCombatAbility(model, combat, performer, target);
        } else {
            combat.println(performer.getFirstName() + " is too exhausted to perform a " + getName().toLowerCase() + ".");
            takeAnotherAction = true;
        }
    }

    protected abstract void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target);
}
