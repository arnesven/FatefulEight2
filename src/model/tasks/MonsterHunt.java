package model.tasks;

import model.enemies.Enemy;
import model.enemies.WerewolfEnemy;

import java.awt.*;
import java.io.Serializable;

public class MonsterHunt implements Serializable {
    private final String turnInPlaceName;
    private final Destination destination;
    private final int reward;
    private Enemy monster;
    private boolean tracked = false;

    public MonsterHunt(String turnInPlaceName, Destination destination, Enemy enemy, int reward) {
        this.turnInPlaceName = turnInPlaceName;
        monster = enemy;
        this.destination = destination;
        this.reward = reward;
    }

    public int getReward() {
        return reward;
    }

    public Enemy getMonster() {
        return monster;
    }

    public String getDestination() {
        return destination.getLongDescription();
    }

    public String getShortDescription() {
        return destination.getShortDescription();
    }

    public Point getPosition() {
        return destination.getPosition();
    }

    public String getTurnInTownName() {
        return turnInPlaceName;
    }

    public void setTracked(boolean b) {
        this.tracked = b;
    }

    public boolean isTracked() {
        return tracked;
    }
}
