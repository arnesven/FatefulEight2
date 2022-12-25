package model.enemies;

import view.sprites.Sprite;

import java.awt.*;

public abstract class BigEnemy extends Enemy {
    public BigEnemy(char enemyGroup, String name) {
        super(enemyGroup, name);
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public Point getCursorShift() {
        return new Point(2, 0);
    }
}
