package model.map;

import view.MyColors;

public class DesertHills extends DesertHex {
    public DesertHills(int roads, int rivers) {
        super(roads, rivers, new HillsLocation(MyColors.YELLOW));
    }

    @Override
    public String getTerrainName() {
        return "desert hills";
    }
}
