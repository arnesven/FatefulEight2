package model.combat.conditions;

import model.Model;
import model.combat.Combatant;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;

public class BleedingCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC1), MyColors.RED, MyColors.BLACK, MyColors.CYAN);

    public BleedingCondition() {
        super("Bleeding", "BLD");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        super.endOfCombatRoundTrigger(model, state, comb);
        state.println(comb.getName() + " takes 1 damage from bleeding.");
        comb.addToHP(-1);
        if (state instanceof CombatEvent) {
            ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.MAGICAL_DAMAGE);
        }
        if (comb.isDead()) {
            state.println(comb.getName() + " has bled out!");
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this combatant is bleeding and " +
                "will suffer damage each combat round until healed.");
    }
}
