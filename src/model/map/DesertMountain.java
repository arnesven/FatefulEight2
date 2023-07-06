package model.map;

import view.MyColors;

public class DesertMountain extends MountainHex {
    public DesertMountain(int roads, int rivers, int state) {
        super(roads, rivers, state);
        setColor(MyColors.YELLOW);
    }

    @Override
    public String getTerrainName() {
        return "desert mountains";
    }
}
