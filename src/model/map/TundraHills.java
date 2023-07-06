package model.map;

import view.MyColors;

public class TundraHills extends TundraHex {
    public TundraHills(int roads, int rivers, int state) {
        super(roads, rivers, new HillsLocation(MyColors.WHITE), state);
    }

    @Override
    public String getTerrainName() {
        return "tundra hills";
    }
}
