package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.spells.FullRoundSpell;
import model.states.CombatEvent;
import model.states.GameState;
import sound.SoundEffects;
import util.MyRandom;

public class EnemyCastingSpellCondition extends CastingFullRoundSpellCondition {
    public EnemyCastingSpellCondition(Combatant combatant, int castRound) {
        super(new DummySpell(), combatant, null, castRound);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void wasAttackedBy(GameCharacter subject, CombatEvent combat, Enemy enemy, int damage) {
        if (damage > 0) {
            if (MyRandom.flipCoin()) {
                enemy.removeCondition(EnemyCastingSpellCondition.class);
                combat.println(enemy.getName() + "'s concentration was broken and " +
                        "is no longer casting the spell.");
                combat.println(enemy.getName() + " takes 1 damage from spell feedback!");
                SoundEffects.playSpellFail();
                enemy.addCondition(new ErodeCondition());
                combat.doDamageToEnemy(enemy, 1, subject);
                enemy.removeCondition(ErodeCondition.class);
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
