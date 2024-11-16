package model.items.imbuements;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.spells.ChainLightningSpell;
import model.states.CombatEvent;
import util.MyRandom;

public class ChainLightningImbuement extends WeaponImbuement {

    private final ChainLightningSpell spell;

    public ChainLightningImbuement() {
        this.spell = new ChainLightningSpell();
    }

    @Override
    public String getText() {
        return "50% chance to cast chain lightning on hit.";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.flipCoin()) {
            spell.applyCombatEffect(model, combatEvent, gameCharacter, target);
        }
    }
}
