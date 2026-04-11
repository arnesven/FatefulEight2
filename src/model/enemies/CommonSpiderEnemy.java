package model.enemies;

import view.MyColors;
import view.sprites.Sprite;

public class CommonSpiderEnemy extends SpiderEnemy {
    private static final Sprite GRAY_SPRITE = new SpiderSprite(MyColors.DARK_GRAY, MyColors.LIGHT_GRAY,
            MyColors.RED, MyColors.GRAY);

    public CommonSpiderEnemy(char a) {
        super(a);
    }

    @Override
    public SpiderEnemy copy() {
        return new CommonSpiderEnemy(getEnemyGroup());
    }

    @Override
    protected Sprite getSprite() {
        return GRAY_SPRITE;
    }
}
