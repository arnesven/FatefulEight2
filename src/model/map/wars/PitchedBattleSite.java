package model.map.wars;

import view.MyColors;

import java.awt.*;
import java.io.Serializable;


public class PitchedBattleSite implements Serializable {
    public Point position;
    public MyColors groundColor;
    public String name;

    public PitchedBattleSite(Point pos, MyColors color, String name) {
        this.position = pos;
        this.groundColor = color;
        this.name = name;
    }
}
