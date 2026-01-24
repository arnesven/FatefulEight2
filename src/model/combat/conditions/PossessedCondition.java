package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class PossessedCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD5), MyColors.DARK_RED, MyColors.BLACK, MyColors.CYAN);

    public PossessedCondition() {
        super("Possessed", "POS");
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
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this person is currently being possessed by something or someone.");
    }
}
