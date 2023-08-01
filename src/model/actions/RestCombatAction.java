package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.states.CombatEvent;
import util.MyRandom;

public class RestCombatAction extends CombatAction {
    private boolean another;

    public RestCombatAction() {
        super("Rest");
        another = false;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().combatResting(model);
        boolean fullHP = performer.getHP() == performer.getMaxHP();
        boolean fullSP = performer.getSP() == performer.getMaxSP();
        if (fullHP && fullSP) {
            combat.println(performer.getFirstName() + " is already fully rested!");
            this.another = true;
        } else if (fullHP) {
            recoverSP(combat, performer);
        } else if (fullSP) {
            recoverHP(combat, performer);
        } else {
            if (MyRandom.flipCoin()) {
                recoverHP(combat, performer);
            } else {
                recoverSP(combat, performer);
            }
        }
    }

    private void recoverHP(CombatEvent combat, GameCharacter performer) {
        combat.println(performer.getFirstName() + " recovered 1 Health Point.");
        performer.addToHP(1);
    }

    private void recoverSP(CombatEvent combat, GameCharacter performer) {
        combat.println(performer.getFirstName() + " recovered 1 Stamina Point.");
        performer.addToSP(1);
    }

    @Override
    public boolean takeAnotherAction() {
        return another;
    }
}
