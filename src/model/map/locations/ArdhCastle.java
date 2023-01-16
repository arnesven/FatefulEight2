package model.map.locations;

import model.map.CastleLocation;
import view.MyColors;

import java.awt.*;

public class ArdhCastle extends CastleLocation {
    public ArdhCastle() {
        super("Castle Ardh", MyColors.BLUE, "Count Aldeck");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 6);
    }
}
