package model.items.spells;

import model.Model;
import model.combat.BurningWeaponCondition;
import model.combat.Combatant;
import model.combat.Condition;
import model.states.GameState;
import view.sprites.Sprite;

class BlackPactCondition extends Condition {
    public BlackPactCondition() {
        super("Black Pact", "BLP");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return BurningWeaponCondition.CONDITION_SPRITE;
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        comb.removeCondition(BlackPactCondition.class);
    }
}
