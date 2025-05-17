package model.map;

import model.enemies.Enemy;

import java.awt.*;

public class MonsterHuntData {
    private final Point point;
    private final String dwelling;
    private final Enemy enemy;
    private int reward;

    public MonsterHuntData(Point p, String dwelling, Enemy enemy, int distance) {
        this.point = p;
        this.dwelling = dwelling;
        this.enemy = enemy;
        this.reward = 3 * enemy.getThreat() / 2 + enemy.getThreat() / 2 * distance;
    }

    public Point getPosition() {
        return point;
    }

    public String getDwelling() {
        return dwelling;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public int getReward() {
        return reward;
    }
}
