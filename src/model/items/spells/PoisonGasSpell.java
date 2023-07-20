package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.Condition;
import model.enemies.AutomatonEnemy;
import model.enemies.Enemy;
import model.enemies.UndeadEnemy;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.*;

public class PoisonGasSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(0, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public PoisonGasSpell() {
        super("Poison Gas", 20, MyColors.BLACK, 8, 1, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PoisonGasSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (target instanceof UndeadEnemy || target instanceof AutomatonEnemy ||
                target.hasCondition(EnemyPoisonCondition.class)) {
            combat.println(getName() + " has no effect on " + target.getName());
            return;
        }
        combat.addSpecialEffect(target, new GreenSmokeAnimation());
        combat.println(target.getName() + " has been poisoned!");
        target.addCondition(new EnemyPoisonCondition(performer));
    }

    @Override
    public String getDescription() {
        return "Releases a poisonous gas cloud that damages enemies over time.";
    }

    private static final Sprite CONDITION_SPRITE = CharSprite.make((char)(0xC1), MyColors.GREEN, MyColors.BLACK, MyColors.CYAN);

    private static class EnemyPoisonCondition extends Condition {
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
                ((CombatEvent)state).doDamageToEnemy(comb, 1, caster);
            } else {
                throw new IllegalStateException("EndOfCombatRoundTrigger was called with something other than combat event!");
            }
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }
    }

    private static class GreenSmokeAnimation extends RunOnceAnimationSprite {
        public GreenSmokeAnimation() {
            super("greensmokeball", "combat.png", 0, 16, 32, 32, 8, MyColors.LIGHT_GREEN);
            setColor1(MyColors.WHITE);
        }
    }
}
