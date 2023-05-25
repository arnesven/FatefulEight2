package model.enemies;

import java.awt.Point;

public abstract class BigBeastEnemy extends BeastEnemy {
    public BigBeastEnemy(char enemyGroup, String name, int aggressiveness) {
        super(enemyGroup, name, aggressiveness);
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
