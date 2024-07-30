package model.headquarters;

import model.Model;

import java.awt.*;
import java.io.Serializable;

import static model.headquarters.Headquarters.*;

public abstract class HeadquarterAppearance implements Serializable {

    public static HeadquarterAppearance createAppearance(int size) {
        switch (size) {
            case SMALL_SIZE:
                return new SmallHeadquarters();
            case MEDIUM_SIZE:
                return new MediumHeadquarters();
            case LARGE_SIZE:
                return new LargeHeadquarters();
            case GRAND_SIZE:
                return new GrandHeadquarters();
            case MAJESTIC_SIZE:
                return new MajesticHeadquarters();
            default:
                throw new IllegalArgumentException("Headquarters size not supporter: " + size);
        }
    }

    public abstract void drawYourself(Model model, Point p);
    public abstract String getDescription();

}
