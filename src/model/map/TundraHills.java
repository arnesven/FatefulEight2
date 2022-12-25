package model.map;

import view.MyColors;

public class TundraHills extends TundraHex {
    public TundraHills(int roads, int rivers) {
        super(roads, rivers, new HillsLocation(MyColors.WHITE));
    }

    @Override
    public String getTerrainName() {
        return "tundra hills";
    }
}
