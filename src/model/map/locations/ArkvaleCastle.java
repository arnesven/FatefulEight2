package model.map.locations;

import model.map.CastleLocation;
import view.MyColors;

import java.awt.*;

public class ArkvaleCastle extends CastleLocation {
    public ArkvaleCastle() {
        super("Arkvale Castle", MyColors.WHITE, "Queen Valstine");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 2);
    }

}
