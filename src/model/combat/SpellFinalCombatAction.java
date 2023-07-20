package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

public class SpellFinalCombatAction extends CombatAction {
    private final CombatSpell spell;

    public SpellFinalCombatAction(CombatSpell spell) {
        super(spell.getName());
        this.spell = spell;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        boolean success = spell.castYourself(model, combat, performer);
        if (success) {
            combat.addSpecialEffect(performer, new CastingEffectSprite());
            spell.applyCombatEffect(model, combat, performer, target);
            if (target instanceof GameCharacter) {
                ((GameCharacter) target).addToAttitude(performer, 2);
            }
        } else {
            combat.addSpecialEffect(performer, new MiscastEffectSprite());
        }
    }
}
