package model.tasks;

import java.awt.*;
import java.io.Serializable;

public class Destination implements Serializable {
    private final Point position;
    private final String longDescription;
    private final String shortDescription;

    public Destination(Point p, String longD, String shortD) {
        this.position = p;
        this.longDescription = longD;
        this.shortDescription = shortD;
    }

    public Point getPosition() {
        return position;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
