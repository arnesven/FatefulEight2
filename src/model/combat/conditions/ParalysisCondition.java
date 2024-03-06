package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ParalysisCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC0), MyColors.LIGHT_YELLOW, MyColors.BLACK, MyColors.CYAN);
    private static final String HELP_TEXT = "Paralysis is a condition which prevents a combatant from performing any actions during combat.";

    public ParalysisCondition() {
        super("Paralysis", "PAR");
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, HELP_TEXT);
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }
}
