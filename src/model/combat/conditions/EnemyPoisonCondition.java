package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.spells.PoisonGasSpell;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;

public class EnemyPoisonCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char)(0xC1), MyColors.GREEN, MyColors.BLACK, MyColors.CYAN);

    private final GameCharacter caster;

    public EnemyPoisonCondition(GameCharacter caster) {
        super("Poison", "PSN");
        this.caster = caster;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        state.println(comb.getName() + " takes suffers 1 damage from the effects of the poison.");
        if (state instanceof CombatEvent) {
            ((CombatEvent) state).doDamageToEnemy(comb, 1, caster);
            ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.MAGICAL_DAMAGE);
        } else {
            throw new IllegalStateException("EndOfCombatRoundTrigger was called with something other than combat event!");
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new PoisonCondition().getHelpView(view);
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }
}
