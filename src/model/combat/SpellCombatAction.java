package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.items.UsableItem;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.StrikeEffectSprite;

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

    private static class CastingEffectSprite extends RunOnceAnimationSprite {
        public CastingEffectSprite() {
            super("castingeffect", "combat.png", 0, 9, 32, 32, 8, MyColors.WHITE);
            setColor1(MyColors.WHITE);
            setColor2(MyColors.LIGHT_YELLOW);
            setColor3(MyColors.LIGHT_PINK);
            setColor4(MyColors.CYAN);
        }
    }

    private static class MiscastEffectSprite extends RunOnceAnimationSprite {
        public MiscastEffectSprite() {
            super("miscasteffect", "combat.png", 0, 10, 32, 32, 8, MyColors.WHITE);
            setColor1(MyColors.WHITE);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.WHITE);
            setColor4(MyColors.TAN);
        }
    }
}
