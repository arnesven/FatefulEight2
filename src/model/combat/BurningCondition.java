package model.combat;

import model.Model;
import model.characters.GameCharacter;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;

public class BurningCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xDA), MyColors.LIGHT_YELLOW, MyColors.ORANGE, MyColors.CYAN);
    private final GameCharacter applier;

    public BurningCondition(GameCharacter applier) {
        super("Burning", "BRN");
        setDuration(Integer.MAX_VALUE);
        this.applier = applier;
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
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        if (state instanceof CombatEvent) {
            state.println(comb.getName() + " takes 1 damage from burning.");
            ((CombatEvent) state).doDamageToEnemy(comb, 1, applier);
            ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.MAGICAL_DAMAGE);
        }
        if (MyRandom.flipCoin()) {
            setDuration(0);
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "Burning condition represents that " +
                "the target has been set on fire. This condition deals 1 damage to the target each round. " +
                "Each round there is a 50% chance that the fire goes out.");
    }
}
