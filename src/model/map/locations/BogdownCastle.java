package model.map.locations;

import model.map.CastleLocation;
import view.MyColors;

import java.awt.*;

public class BogdownCastle extends CastleLocation {
    public BogdownCastle() {
        super("Bogdown Castle", MyColors.DARK_GREEN, "King Burod");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(1, 2);
    }

}
