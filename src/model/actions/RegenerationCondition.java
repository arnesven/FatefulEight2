package model.actions;

import model.Model;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;

public class RegenerationCondition extends Condition {

    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.LIGHT_GREEN, MyColors.BLACK, MyColors.GREEN);
    private final int magnitude;

    public RegenerationCondition(int duration, int magnitude) {
        super("Regeneration", "RGN");
        setDuration(duration + 1);
        this.magnitude = magnitude;
    }

    public RegenerationCondition(int duration) {
        this(duration, 1);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        if (comb.getHP() < comb.getMaxHP() && !comb.isDead()) {
            state.println(comb.getName() + " regenerates " + magnitude + " Health Point" + (magnitude > 1 ? "s":"") + ".");
            comb.addToHP(magnitude);
            if (state instanceof CombatEvent) {
                ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.HEALING);
            }
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition which indicates that the combatant is " +
                        "currently regenerating health points each turn.");
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }
}
