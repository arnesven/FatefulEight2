package model.combat.conditions;

import model.Model;
import model.combat.Combatant;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WardCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD1), MyColors.WHITE, MyColors.PURPLE, MyColors.CYAN);
    private int turnsLeft;

    public WardCondition() {
        super("Ward", "WRD");
        this.turnsLeft = 2;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        turnsLeft--;
        if (turnsLeft == 0) {
            comb.removeCondition(WardCondition.class);
        }
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this combatant is currently protected from magic damage.");
    }
}
