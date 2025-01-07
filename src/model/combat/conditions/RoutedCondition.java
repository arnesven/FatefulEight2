package model.combat.conditions;

import model.Model;
import model.combat.Combatant;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class RoutedCondition extends Condition {
    public static final int DEFAULT_ESCAPE_CHANCE = 7;
    private static final Sprite SPRITE = CharSprite.make((char)(0xD5), MyColors.BLUE, MyColors.WHITE, MyColors.PURPLE);
    private int fleeChance;

    public RoutedCondition(int fleeChance) {
        super("Routed", "RTD");
        this.fleeChance = fleeChance;
    }

    public RoutedCondition() {
        this(DEFAULT_ESCAPE_CHANCE);
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
                "An enemy with the Routed condition will attempt to flee combat as quickly as they can.");
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        super.endOfCombatRoundTrigger(model, state, comb);
        if (MyRandom.rollD10() > 10 - fleeChance) {
            state.println(comb.getName() + " flees from combat.");
            if (state instanceof CombatEvent) {
                ((CombatEvent) state).retreatEnemy(comb);
            }
        } else {
            state.println(comb.getName() + " attempted to flee the combat, but couldn't get away.");
        }
    }
}
