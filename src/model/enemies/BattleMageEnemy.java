package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.EnemyCastingSpellCondition;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.*;
import model.races.Race;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

public class BattleMageEnemy extends HumanoidEnemy {
    private final BattleMageSprite sprite;

    public BattleMageEnemy(char b) {
        super(b, "Battle Mage");
        this.sprite = new BattleMageSprite(Race.randomRace().getColor());
        setAttackBehavior(new BattleMageAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    private static class BattleMageSprite extends LoopingSprite {
        public BattleMageSprite(MyColors skinColor) {
            super("battlemage", "enemies.png", 0x14A, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.RED);
            setColor3(skinColor);
            setColor4(MyColors.LIGHT_GRAY);
        }
    }

    private static class BattleMageAttackBehavior extends EnemyAttackBehavior {

        private EnemyAttackBehavior currentBehavior;
        public BattleMageAttackBehavior() {
            currentBehavior = randomNextBehavior();
        }

        @Override
        public boolean isPhysicalAttack() {
            return currentBehavior.isPhysicalAttack();
        }

        @Override
        public boolean isCriticalHit() {
            return currentBehavior.isCriticalHit();
        }

        @Override
        public RunOnceAnimationSprite getStrikeEffect() {
            return currentBehavior.getStrikeEffect();
        }

        @Override
        public String getSound() {
            return currentBehavior.getSound();
        }

        @Override
        public void announceRangedAttack(Model model, CombatEvent combatEvent, Enemy enemy, GameCharacter target) {
            currentBehavior.announceRangedAttack(model, combatEvent, enemy, target);
        }

        private EnemyAttackBehavior randomNextBehavior() {
            int dieRoll = MyRandom.rollD6();
            if (dieRoll < 4) {
                return new MeleeAttackBehavior();
            }
            if (dieRoll < 6) {
                return new DestructionSpellAttackBehavior();
            }
            return new RestorationSpellAttackBehavior(new MeleeAttackBehavior());
        }

        @Override
        public boolean canAttackBackRow() {
            return currentBehavior.canAttackBackRow();
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            currentBehavior.performAttack(model, enemy, target, combatEvent);
            if (!enemy.hasCondition(EnemyCastingSpellCondition.class)) {
                currentBehavior = randomNextBehavior();
            }
        }

        @Override
        public String getUnderText() {
            return "Spell";
        }
    }
}
