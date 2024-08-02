package model.enemies;

import view.MyColors;

public class BlueGelatinousBlobEnemy extends GelatinousBlobEnemy {
    public BlueGelatinousBlobEnemy(char a) {
        super(a, MyColors.LIGHT_BLUE, MyColors.BLUE);
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new BlueGelatinousBlobEnemy(getEnemyGroup());
    }
}
