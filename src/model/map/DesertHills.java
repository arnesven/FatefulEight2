package model.map;

import view.MyColors;

public class DesertHills extends DesertHex {
    public DesertHills(int roads, int rivers, int state) {
        super(roads, rivers, new HillsLocation(MyColors.YELLOW), state);
    }

    @Override
    public String getTerrainName() {
        return "desert hills";
    }
}
