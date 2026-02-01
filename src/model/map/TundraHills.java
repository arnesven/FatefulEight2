package model.map;

import view.MyColors;

import java.awt.*;

public class TundraHills extends TundraHex {
    public TundraHills(int roads, int rivers, int state) {
        super(roads, rivers, new HillsLocation(MyColors.WHITE), state);
    }

    @Override
    public String getTerrainName() {
        return "tundra hills";
    }

    @Override
    public WorldHex makePastSelf(Point oldPosition, Point newPosition) {
        if (newPosition.y >= 6) {
            return new PastHillsHex(getRivers(), getState());
        }
        return super.makePastSelf(oldPosition, newPosition);
    }
}
