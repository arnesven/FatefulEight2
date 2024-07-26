package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.NoAttackCombatBehavior;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class TrainingDummyEnemy extends Enemy {
    private static final Sprite SPRITE = new Sprite32x32("trainingdummy", "enemies.png", 0x6E,
            MyColors.BLACK, MyColors.BEIGE, MyColors.RED, MyColors.GRAY);

    public TrainingDummyEnemy(char enemyGroup) {
        super(enemyGroup, "Training Dummy", new NoAttackCombatBehavior());
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getDeathSound() {
        return null;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
