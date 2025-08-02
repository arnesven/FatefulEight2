package model.combat.abilities;

import model.combat.conditions.Condition;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

class InspiredCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD8), MyColors.BLACK, MyColors.PINK, MyColors.CYAN);

    private final int bonus;

    public InspiredCondition(int bonus) {
        super("Inspired", "INS");
        setDuration(2);
        this.bonus = bonus;
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
    public int getAttackBonus() {
        return bonus;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicated that this combatant is inspired, " +
                "thus enjoying a bonus to combat rolls.");
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }
}
