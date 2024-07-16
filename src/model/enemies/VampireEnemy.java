package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.combat.loot.BossCombatLoot;
import model.combat.loot.CombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VampireEnemy extends UndeadEnemy {
    private static final LoopingSprite SPRITE = new VampireEnemySprite();

    public VampireEnemy(char a) {
        super(a, "Vampire", new VampireAttackBehavior());
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }

    private static class VampireEnemySprite extends LoopingSprite {
        public VampireEnemySprite() {
            super("vampire", "enemies.png", 0x1C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(MyColors.WHITE);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }

    private static class VampireAttackBehavior extends MeleeAttackBehavior {
        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            int hpBefore = target.getHP();
            super.performAttack(model, enemy, target, combatEvent);
            if (!target.isDead() && hpBefore > target.getHP() && MyRandom.rollD10() == 10) {
                VampirismCondition.makeVampire(model, combatEvent, target);
            }
        }
    }
}
