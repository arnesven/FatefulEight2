package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class GiantGrowthCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.BEIGE, MyColors.BLACK, MyColors.CYAN);
    private final int magnitude;

    public GiantGrowthCondition(int magnitude) {
        super("Giant Growth", "GRW");
        this.magnitude = magnitude;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
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
    public int getAttackBonus() {
        return magnitude;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that the combatant has been targeted " +
                "by the Giant Growth spell.");
    }
}
