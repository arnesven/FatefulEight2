package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class InvisibilityCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.BLACK, MyColors.WHITE, MyColors.CYAN);

    public InvisibilityCondition(int duration) {
        super("Invisible", "INV");
        setDuration(duration+1);
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
        return new ConditionHelpDialog(view, this, "A condition indicating that this combatant is " +
                "invisible and cannot be targeted by attacks.");
    }
}
