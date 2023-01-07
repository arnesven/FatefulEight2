package model.map;

import java.io.Serializable;

public class WaterPath implements Serializable {
    private final WorldHex hex;
    private final int direction;

    public WaterPath(WorldHex hex, int direction) {
        this.hex = hex;
        this.direction = direction;
    }
}
