package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ExposedCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char) (0xC2), MyColors.GREEN, MyColors.BLACK, MyColors.CYAN);
    private static final String TEXT = "A condition indicating that this combatant has its defense lowered and is exposed to attacks.\n\n" +
            "Combatants attacking an exposed target enjoy a +2 bonus to their attack rolls.";

    public ExposedCondition() {
        super("Exposed", "EXP");
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
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, TEXT);
    }
}
