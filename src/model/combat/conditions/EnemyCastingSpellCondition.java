package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.enemies.behaviors.SpellAttackBehavior;
import model.items.spells.FullRoundSpell;
import model.states.CombatEvent;
import model.states.GameState;
import sound.SoundEffects;
import util.MyRandom;
import view.sprites.DamageValueEffect;

public class EnemyCastingSpellCondition extends CastingFullRoundSpellCondition {
    private final SpellAttackBehavior behavior;

    public EnemyCastingSpellCondition(Combatant combatant, SpellAttackBehavior behavior, int castRound) {
        super(new DummySpell(), combatant, null, castRound);
        this.behavior = behavior;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void wasAttackedBy(GameCharacter subject, CombatEvent combat, Enemy enemy, int damage) {
        if (damage > 0) {
            if (MyRandom.rollD6() > 4) {
                enemy.removeCondition(EnemyCastingSpellCondition.class);
                combat.println(enemy.getName() + "'s concentration was broken and " +
                        "is no longer casting the spell.");
                if (behavior.getFeedbackDamage() > 0) {
                    combat.println(enemy.getName() + " takes " + behavior.getFeedbackDamage() + " damage from spell feedback!");
                    SoundEffects.playSpellFail();
                    enemy.addCondition(new ErodeCondition());
                    combat.doDamageToEnemy(enemy, behavior.getFeedbackDamage(), subject);
                    combat.addFloatyDamage(enemy, behavior.getFeedbackDamage(), DamageValueEffect.MAGICAL_DAMAGE);
                    enemy.removeCondition(ErodeCondition.class);
                }
            }
        }
    }

    private static class DummySpell implements FullRoundSpell {
        @Override
        public void castingComplete(Model model, GameState state, Combatant performer, Combatant target) {

        }

        @Override
        public int getCastTime() {
            return 999;
        }
    }
}
