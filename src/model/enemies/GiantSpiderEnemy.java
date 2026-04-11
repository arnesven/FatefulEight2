package model.enemies;

import model.enemies.behaviors.ParalysisAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;

public class GiantSpiderEnemy extends SpiderEnemy {

    private static final Sprite BLACK_SPRITE = new SpiderSprite(MyColors.DARK_GRAY, MyColors.BLACK,
            MyColors.YELLOW, MyColors.DARK_PURPLE);

    public GiantSpiderEnemy(char a) {
        super(a, "Giant Spider", HOSTILE, new ParalysisAttackBehavior(3));
    }

    @Override
    protected Sprite getSprite() {
        return BLACK_SPRITE;
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getPhysicalDamageReduction() {
        return 1;
    }

    @Override
    public SpiderEnemy copy() {
        return new GiantSpiderEnemy(getEnemyGroup());
    }
}
