package model.items.spells;

import model.Model;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class BlackPactCondition extends Condition {
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

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this character has performed the Black Pact spell and that " +
                        "health point costs of spells is currently decreased for this character.");
    }
}
