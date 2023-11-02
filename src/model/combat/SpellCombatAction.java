package model.combat;

import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.items.spells.AuxiliarySpell;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

import java.util.ArrayList;
import java.util.List;

public class SpellCombatAction extends BasicCombatAction {
    private final List<CombatSpell> combatSpells;
    private final Combatant target;

    public SpellCombatAction(List<CombatSpell> combatSpells, Combatant target) {
        super("Spell", false);
        this.combatSpells = combatSpells;
        this.target = target;
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
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
                res.add(new SpellFinalCombatAction(spell));
            }
        }
        return res;
    }

}
