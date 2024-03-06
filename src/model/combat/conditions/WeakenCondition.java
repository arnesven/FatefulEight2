package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WeakenCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char) (0xC2), MyColors.LIGHT_RED, MyColors.BLACK, MyColors.CYAN);

    public WeakenCondition(int duration) {
        super("Weakened", "WKN");
        setDuration(duration + 1);
    }

    public WeakenCondition() {
        this(3);
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
                "A condition indicating that this combatant's combat power " +
                "is currently degraded. A weakened combatant will do less damage in combat.");
    }
}
