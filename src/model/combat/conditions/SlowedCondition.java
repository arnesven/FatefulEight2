package model.combat.conditions;

import model.Model;
import model.combat.Combatant;
import model.states.GameState;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class SlowedCondition extends Condition {
    private static final int DAYS_DURATION = 3;
    private final int amount;

    private static final Sprite SPRITE = CharSprite.make((char)(0xC2), MyColors.BROWN, MyColors.WHITE, MyColors.CYAN);
    private final int gottenOnDay;

    public SlowedCondition(int amount, int day) {
        super("Slowed", "SLW");
        this.amount = amount;
        this.gottenOnDay = day;
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        if (model.getDay() == gottenOnDay + DAYS_DURATION) {
            comb.removeCondition(SlowedCondition.class);
        }
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
    public int getSpeedBonus() {
        return -amount;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicated that this character has been " +
                        "slowed down and is suffering a " + ((amount > 0) ? ("-" + amount + " "):"") + "penalty to Speed." +
                        " Standard duration of this condition is " + MyStrings.numberWord(DAYS_DURATION) + " days.");
    }
}
