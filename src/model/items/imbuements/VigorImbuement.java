package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;

public class VigorImbuement extends WeaponImbuement {

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (damage > 0 && target.isDead()) {
            combatEvent.println(gameCharacter.getName() + " absorbed energy from the slain " + target.getName() + " - regained 1 SP!");
            gameCharacter.addToSP(1);
        }
    }

    @Override
    public String getText() {
        return "Gain 1 SP on kill";
    }
}
