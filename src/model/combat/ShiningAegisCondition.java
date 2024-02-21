package model.combat;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ShiningAegisCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD1), MyColors.LIGHT_YELLOW, MyColors.WHITE, MyColors.CYAN);

    public ShiningAegisCondition(int duration) {
        super("Shining Aegis", "AEG");
        setDuration(duration);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    public int getArmorBonus() {
        return 3;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this combatant is currently being protected by a magical shield.");
    }
}
