package model.enemies;

import view.MyColors;

public class BrownGelatinousBlobEnemy extends GelatinousBlobEnemy {
    public BrownGelatinousBlobEnemy(char a) {
        super(a, MyColors.TAN, MyColors.BROWN);
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new BrownGelatinousBlobEnemy(getEnemyGroup());
    }
}
