package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WardCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD1), MyColors.WHITE, MyColors.PURPLE, MyColors.CYAN);

    public WardCondition() {
        super("Ward", "WRD");
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
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this combatant is currently protected from magic damage.");
    }
}
