package model.enemies;

import model.enemies.behaviors.MeleeAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class GoblinWolfRiderEnemy extends GoblinEnemy {
    private static final Sprite SPRITE = new GoblinWolfRiderEnemySprite();

    public GoblinWolfRiderEnemy(char a) {
        super(a, "Goblin Wolf Rider", new MeleeAttackBehavior());
    }

    @Override
    public int getSpeed() {
        return 10;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected int getHeight() {
        return 2;
    }

    @Override
    public GoblinEnemy copy() {
        return new GoblinWolfRiderEnemy(getEnemyGroup());
    }

    private static class GoblinWolfRiderEnemySprite extends LoopingSprite {
        public GoblinWolfRiderEnemySprite() {
            super("wolfriderenemy", "enemies.png", 0x88, 32, 64);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.GRAY);
            setFrames(4);
        }
    }
}
