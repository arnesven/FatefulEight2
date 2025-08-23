package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.EnemyCastingSpellCondition;
import model.enemies.Enemy;
import model.states.CombatEvent;
import sound.SoundEffects;
import sprites.CombatSpeechBubble;
import util.MyRandom;

import java.util.List;

public abstract class SpellAttackBehavior extends EnemyAttackBehavior {

    private final String spellName;
    private final int feedbackDamage;
    private boolean isCasting = false;

    public SpellAttackBehavior(String spellName, int spellFeedbackDamage) {
        this.spellName = spellName;
        this.feedbackDamage = spellFeedbackDamage;
    }

    protected abstract void resolveSpell(Model model, Enemy enemy, GameCharacter target, CombatEvent combat);

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        model.getTutorial().enemyAttacks2(model);
        if (isCasting) {
            enemy.removeCondition(EnemyCastingSpellCondition.class);
            combat.addSpecialEffect(enemy, new CombatSpeechBubble());
            combat.printQuote(enemy.getName(), spellName + "!");
            resolveSpell(model, enemy, target, combat);
            isCasting = false;
        } else {
            isCasting = true;
            enemy.addCondition(new EnemyCastingSpellCondition(enemy, this, combat.getCurrentRound()));
            SoundEffects.playSpellSuccess();
            combat.println(enemy.getName() + " begins casting a spell...");
        }
    }

    @Override
    public void announceRangedAttack(Model model, CombatEvent combatEvent, Enemy enemy, GameCharacter target) {
        // Intentionally do nothing.
    }

    @Override
    public boolean isPhysicalAttack() {
        return false;
    }

    @Override
    public boolean canAttackBackRow() {
        return true;
    }

    @Override
    public String getUnderText() {
        return "Spell";
    }

    @Override
    public String getSound() {
        return "wand";
    }

    @Override
    public boolean isCriticalHit() {
        return false;
    }

    protected List<GameCharacter> getCharacterAndAlliesTargets(Model model, CombatEvent combat,
                                                               Enemy enemy, GameCharacter target, int total) {
        List<GameCharacter> allCombatants = enemy.getCandidateTargets(model, combat);
        while (allCombatants.size() > total) {
            GameCharacter toRemove = MyRandom.sample(allCombatants);
            if (toRemove != target) {
                allCombatants.remove(MyRandom.sample(allCombatants));
            }
        }
        return allCombatants;
    }

    public int getFeedbackDamage() {
        return feedbackDamage;
    }
}
