package model.enemies;

import model.enemies.behaviors.PoisonAttackBehavior;
import view.MyColors;
import view.sprites.Sprite;

public class ForestSpiderEnemy extends SpiderEnemy {
    private static final Sprite GREEN_SPIDER = new SpiderSprite(MyColors.BLACK, MyColors.DARK_GREEN,
            MyColors.RED, MyColors.TAN);

    public ForestSpiderEnemy(char a) {
        super(a);
        setAttackBehavior(new PoisonAttackBehavior(3));
    }

    @Override
    public SpiderEnemy copy() {
        return new ForestSpiderEnemy(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return GREEN_SPIDER;
    }
}
