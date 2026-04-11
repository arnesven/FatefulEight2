package model.enemies;

import view.MyColors;
import view.sprites.Sprite;

public class CaveSpiderEnemy extends SpiderEnemy {
    private static final Sprite BLUE_SPIDER = new SpiderSprite(MyColors.BLACK, MyColors.DARK_BLUE,
            MyColors.LIGHT_PINK, MyColors.DARK_GRAY);

    public CaveSpiderEnemy(char a) {
        super(a);
    }

    @Override
    public int getMagicalDamageReduction() {
        return 1;
    }

    @Override
    public SpiderEnemy copy() {
        return new CaveSpiderEnemy(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return BLUE_SPIDER;
    }
}
