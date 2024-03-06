package model.combat.conditions;

import model.Model;
import model.combat.Combatant;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class RowdyCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC1), MyColors.PINK, MyColors.BLACK, MyColors.CYAN);
    private static final String TEXT = "Combatants with the Rowdy (RWY) condition will retreat from combat at the end of " +
            "a combat round where they have taken damage.";

    public RowdyCondition() {
        super("Rowdy", "RWY");
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
        if (comb.getHP() < comb.getMaxHP() && !comb.isDead()) {
            state.println(comb.getName() + " flees from combat!");
            ((CombatEvent) state).retreatEnemy(comb);
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, TEXT);
    }
}
