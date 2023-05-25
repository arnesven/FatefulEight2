package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

import java.util.ArrayList;
import java.util.List;

public class SpellCombatAction extends CombatAction {
    private final List<CombatSpell> combatSpells;
    private final Combatant target;

    public SpellCombatAction(List<CombatSpell> combatSpells, Combatant target) {
        super("Spell");
        this.combatSpells = combatSpells;
        this.target = target;
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        // unused
    }

    @Override
    public boolean hasInnerMenu() {
        return true;
    }

    @Override
    public List<CombatAction> getInnerActions(Model model) {
        List<CombatAction> res = new ArrayList<>();
        for (CombatSpell spell : combatSpells) {
            if (spell.canBeCastOn(model, target)) {
                res.add(new CombatAction(spell.getName()) {
                    @Override
                    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                        boolean success = spell.castYourself(model, combat, performer);
                        if (success) {
                            combat.addSpecialEffect(performer, new CastingEffectSprite());
                            spell.applyCombatEffect(model, combat, performer, target);
                        } else {
                            combat.addSpecialEffect(performer, new MiscastEffectSprite());
                        }
                    }
                });
            }
        }
        return res;
    }

}
