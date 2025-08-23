package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.EnemyCastingSpellCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import sound.SoundEffects;
import util.MyRandom;

import view.sprites.MagmaBlastEffectSprite;

import java.util.List;

public class DestructionSpellAttackBehavior extends EnemyAttackBehavior {
    private boolean isCasting = false;

    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public boolean isPhysicalAttack() {
        return false;
    }

    @Override
    public void announceRangedAttack(Model model, CombatEvent combatEvent, Enemy enemy, GameCharacter target) {
        // Intentionally do nothing.
    }

    @Override
    public String getSound() {
        return "wand";
    }

    @Override
    public boolean isCriticalHit() {
        return false;
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        if (isCasting) {
            enemy.removeCondition(EnemyCastingSpellCondition.class);
            combat.println(enemy.getName() + " casts Destruction!");
            List<GameCharacter> targets = getTargets(model, combat, enemy, target, 3);
            for (GameCharacter t : targets) {
                combat.print(t.getName() + " was struck by the blast. ");
                combat.addSpecialEffect(t, new MagmaBlastEffectSprite());
                t.getAttackedBy(enemy, model, combat);
            }
            isCasting = false;
        } else {
            isCasting = true;
            enemy.addCondition(new EnemyCastingSpellCondition(enemy, combat.getCurrentRound()));
            SoundEffects.playSpellSuccess();
            combat.println(enemy.getName() + " begins casting a spell...");
        }
    }

    private List<GameCharacter> getTargets(Model model, CombatEvent combatEvent,
                                           Enemy enemy, GameCharacter target, int total) {
        List<GameCharacter> allCombatants = enemy.getCandidateTargets(model, combatEvent);
        while (allCombatants.size() > total) {
            GameCharacter toRemove = MyRandom.sample(allCombatants);
            if (toRemove != target) {
                allCombatants.remove(MyRandom.sample(allCombatants));
            }
        }
        return allCombatants;
    }

    @Override
    public String getUnderText() {
        return "Spell";
    }
}
