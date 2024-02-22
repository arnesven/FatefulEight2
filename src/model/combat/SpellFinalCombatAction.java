package model.combat;

import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.items.spells.CombatSpell;
import model.states.CombatEvent;
import view.YesNoMessageView;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

public class SpellFinalCombatAction extends BasicCombatAction {
    private final CombatSpell spell;
    private boolean cancelled = false;

    public SpellFinalCombatAction(CombatSpell spell) {
        super(spell.getName(), false);
        this.spell = spell;
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (spell.getHPCost() >= performer.getHP()) {
            final boolean[] abort = {false};
            YesNoMessageView confirmDialog = new YesNoMessageView(model.getView(),
                    "WARNING: Casting " + getName() + " may kill " + performer.getFirstName() + "! Abort casting?") {
                @Override
                protected void doAction(Model model) {
                    abort[0] = true;
                }
            };
            model.transitionToDialog(confirmDialog);
            combat.print(" ");
            model.getLog().waitForAnimationToFinish();
            if (abort[0]) {
                cancelled = true;
                return;
            }
        }
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

    @Override
    public boolean takeAnotherAction() {
        return cancelled;
    }
}
