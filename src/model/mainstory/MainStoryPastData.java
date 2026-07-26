package model.mainstory;

import java.awt.*;
import java.io.Serializable;

public class MainStoryPastData implements Serializable {
    public final Point entryPoint;
    public final Point upperLeftCorner;
    public final String capitalCity;
    public final String cityA;
    public final String cityB;
    public final Point despair;
    public final Point anguish;
    public final Point sorrow;
    public final Point desolation;

    public MainStoryPastData(Point entry, Point ulCorner, String capital, String cityA, String cityB,
                             Point despairPoint, Point anguishPoint, Point sorrowPoint, Point desolationPoint) {
        entryPoint = entry;
        upperLeftCorner = ulCorner;
        capitalCity = capital;
        this.cityA = cityA;
        this.cityB = cityB;
        despair = despairPoint;
        anguish = anguishPoint;
        sorrow = sorrowPoint;
        desolation = desolationPoint;
    }
}
