package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.DamageValueEffect;

public class AbsorptionImbuement extends WeaponImbuement {

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.rollD10() > 8) {
            combatEvent.println("Absorbed " + damage + " health points from " + target.getName() + ".");
            gameCharacter.addToHP(damage);
            combatEvent.addFloatyDamage(gameCharacter, damage, DamageValueEffect.HEALING);
        }
    }

    @Override
    public String getText() {
        return "20% Chance to absorb damage as HP";
    }
}
