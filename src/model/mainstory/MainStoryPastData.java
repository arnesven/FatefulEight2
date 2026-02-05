package model.mainstory;

import java.awt.*;
import java.io.Serializable;

public class MainStoryPastData implements Serializable {
    public final Point entryPoint;
    public final Point upperLeftCorner;
    public final String capitalCity;
    public final String cityA;
    public final String cityB;

    public MainStoryPastData(Point entry, Point ulCorner, String capital, String cityA, String cityB) {
        entryPoint = entry;
        upperLeftCorner = ulCorner;
        capitalCity = capital;
        this.cityA = cityA;
        this.cityB = cityB;
    }
}
