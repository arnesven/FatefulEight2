package model.enemies;

import view.MyColors;
import view.sprites.Sprite;

public class OlegOgreEnemy extends TrollEnemy {
    private static final Sprite SPRITE = new TrollEnemySprite(MyColors.TAN, MyColors.GOLD);

    public OlegOgreEnemy(char a) {
        super(a);
        setName("Oleg");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 18;
    }
}
