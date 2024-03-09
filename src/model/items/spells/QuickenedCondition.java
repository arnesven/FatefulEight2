package model.items.spells;

import model.combat.conditions.Condition;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class QuickenedCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char)(0xC0), MyColors.CYAN, MyColors.BLACK, MyColors.CYAN);

    public QuickenedCondition(int duration) {
        super("Quickened", "QCK");
        setDuration(duration);
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
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this character " +
                "has a temporary boost to his or her speed.");
    }

}
