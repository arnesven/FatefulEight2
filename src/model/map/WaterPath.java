package model.map;

import java.io.Serializable;

public class WaterPath implements Serializable {
    private final WorldHex hex;
    private WorldHex otherHex;
    private final int direction;
    private int distance = -1;

    public WaterPath(WorldHex hex, int direction) {
        this.hex = hex;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public WorldHex getHex() {
        return hex;
    }

    public void clearDistance() {
        distance = -1;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setOtherHex(WorldHex hex) {
        this.otherHex = hex;
    }

    public WorldHex getOtherHex() {
        return otherHex;
    }

    public boolean isDistanceUnset() {
        return distance == -1;
    }

    public int getDistance() {
        return distance;
    }
}
