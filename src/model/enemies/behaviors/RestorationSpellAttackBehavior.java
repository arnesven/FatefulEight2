package model.enemies.behaviors;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.DamageValueEffect;
import view.sprites.ShinyRingEffect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RestorationSpellAttackBehavior extends SpellAttackBehavior {

    private final EnemyAttackBehavior normalAttack;

    public RestorationSpellAttackBehavior(EnemyAttackBehavior backupAttack) {
        super("Restoration", 0);
        this.normalAttack = backupAttack;
    }

    @Override
    protected void resolveSpell(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        Enemy e = findMostWoundedEnemy(combat);
        if (e == null) {
            combat.println("But the spell had no effect.");
            return;
        }
        recoverHp(combat, e);
    }

    private void recoverHp(CombatEvent combat, Enemy e) {
        int hpBefore = e.getHP();
        e.addToHP(MyRandom.randInt(5, 8));
        int totalRecovered = e.getHP() - hpBefore;
        combat.println(e.getName() + " recovers " + totalRecovered + " HP!");
        combat.addSpecialEffect(e, new ShinyRingEffect());
        combat.addFloatyDamage(e, totalRecovered, DamageValueEffect.HEALING);
    }

    @Override
    public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combat) {
        if (findMostWoundedEnemy(combat) != null) {
            super.performAttack(model, enemy, target, combat);
        } else {
            normalAttack.performAttack(model, enemy, target, combat);
        }
    }

    private Enemy findMostWoundedEnemy(CombatEvent combat) {
        List<Enemy> enms = new ArrayList<>(combat.getEnemies());
        enms.sort(Comparator.comparingInt(Combatant::getHP));
        if (enms.getFirst().getHP() == enms.getFirst().getMaxHP()) {
            return null;
        }
        return enms.getFirst();
    }

    @Override
    public boolean canAttackBackRow() {
        return normalAttack.canAttackBackRow();
    }
}
