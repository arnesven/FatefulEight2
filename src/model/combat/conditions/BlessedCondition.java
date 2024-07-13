package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class BlessedCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xDB), MyColors.WHITE, MyColors.YELLOW, MyColors.CYAN);
    private static final String HELP_TEXT = "A condition indicating that this character has had his or " +
            "her maximum Stamina Points temporarily raised.";
    private final int removeOnDay;

    public BlessedCondition(int removeOnDay) {
        super("Blessed", "BLS");
        this.removeOnDay = removeOnDay;
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
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        if (model.getDay() == this.removeOnDay) {
            comb.removeCondition(BlessedCondition.class);
            state.println(comb.getName() + " is no longer blessed.");
            if (comb instanceof GameCharacter) {
                GameCharacter gc = (GameCharacter)comb;
                if (gc.getSP() > gc.getMaxSP()) {
                    gc.addToSP(gc.getMaxSP() - gc.getSP());
                }
            }
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, new BlessedCondition(0), HELP_TEXT);
    }

    @Override
    public int getStaminaBonus() {
        return 1;
    }
}
