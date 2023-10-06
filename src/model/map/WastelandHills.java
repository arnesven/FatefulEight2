package model.map;

import view.MyColors;

public class WastelandHills extends WastelandHex {
    public WastelandHills(int roads, int rivers, int state) {
        super(roads, rivers, new HillsLocation(MyColors.TAN), state);
    }

    @Override
    public String getTerrainName() {
        return "wasteland hills";
    }
}
