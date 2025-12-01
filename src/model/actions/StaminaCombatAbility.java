package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyStrings;

public abstract class StaminaCombatAbility extends SpecialAbilityCombatAction {
    private boolean takeAnotherAction = false;

    public StaminaCombatAbility(String name, boolean isMeleeAttack) {
        super(name, true, isMeleeAttack);
    }

    @Override
    public boolean takeAnotherAction() {
        return takeAnotherAction;
    }

    @Override
    public final void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof GameCharacter) { // TODO: But riposte you do on yourself...
            combat.println("You can't do a " + getName() + " on a friendly character!");
            takeAnotherAction = true;
        } else if (performer.getSP() > 0) {
            performer.addToSP(-1);
            combat.print(performer.getFirstName() + " exhausts 1 Stamina Point. ");
            doStaminaCombatAbility(model, combat, performer, target);
        } else {
            combat.println(performer.getFirstName() + " is too exhausted to do that.");
            takeAnotherAction = true;
        }
    }

    protected abstract void doStaminaCombatAbility(Model model, CombatEvent combat, GameCharacter performer, Combatant target);
}
