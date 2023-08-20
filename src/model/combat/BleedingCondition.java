package model.combat;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.CharSprite;
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
        if (comb.isDead()) {
            state.println(comb.getName() + " has bled out!");
        }
    }
}
