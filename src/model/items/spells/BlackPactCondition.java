package model.items.spells;

import model.Model;
import model.combat.Combatant;
import model.combat.Condition;
import model.states.GameState;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

class BlackPactCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD2), MyColors.BLACK, MyColors.RED, MyColors.CYAN);

    public BlackPactCondition() {
        super("Black Pact", "BLP");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        comb.removeCondition(BlackPactCondition.class);
    }
}
