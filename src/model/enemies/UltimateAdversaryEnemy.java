package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.characters.special.WitchKingAppearance;
import model.classes.Classes;
import model.combat.loot.BossCombatLoot;
import model.combat.loot.CombatLoot;
import model.enemies.behaviors.*;
import model.races.Race;
import model.states.CombatEvent;
import util.MyRandom;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

import java.util.List;

public class UltimateAdversaryEnemy extends HumanoidEnemy {

    private static final List<EnemyAttackBehavior> ALL_BEHAVIORS = List.of(
            new MultiAttackBehavior(6), new MultiMagicAttackBehavior(4),
            new MultiMagicRangedAttackBehavior(2), new MultiRangedAttackBehavior(4),
            new MultiKnockBackBehavior(5, 2), new KnockBackAttackBehavior(5, 3),
            new DeadlyStrikeBehavior(2.0));

    private static final Sprite SPRITE = Classes.WITCH_KING.getAvatar(Race.WITCH_KING, new WitchKingAppearance());

    public UltimateAdversaryEnemy(char a) {
        super(a, "Ultimate Adversary", new UltimateAdversaryAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 99;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }

    private static class UltimateAdversaryAttackBehavior extends EnemyAttackBehavior {

        private EnemyAttackBehavior currentBehavior = null;

        public UltimateAdversaryAttackBehavior() {
            switchBehavior();
        }

        public void switchBehavior() {
            do {
                EnemyAttackBehavior newBehavior = MyRandom.sample(ALL_BEHAVIORS);
                if (newBehavior != currentBehavior) {
                    currentBehavior = newBehavior;
                    break;
                }
            } while (true);
        }

        public boolean canAttackBackRow() {
            return currentBehavior.canAttackBackRow();
        }

        @Override
        public int calculateDamage(Enemy enemy, boolean isRanged) {
            int damage = currentBehavior.calculateDamage(enemy, isRanged);
            if (isRanged) {
                return (int)(Math.ceil(((double)damage) / 2.0));
            }
            return damage;
        }

        public boolean isCriticalHit() {
            return currentBehavior.isCriticalHit();
        }

        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            currentBehavior.performAttack(model, enemy, target, combatEvent);
            switchBehavior();
        }

        public RunOnceAnimationSprite getStrikeEffect() {
            return currentBehavior.getStrikeEffect();
        }

        public boolean isPhysicalAttack() {
            return currentBehavior.isPhysicalAttack();
        }

        public int numberOfAttacks() {
            return currentBehavior.numberOfAttacks();
        }

        @Override
        public String getUnderText() {
            return "Special";
        }
    }
}
