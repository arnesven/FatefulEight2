package model.map;

import model.map.locations.SwampMountainLocation;

public class SwampMountain extends SwampHex {
    public SwampMountain(int roads, int rivers, int state) {
        super(roads, rivers, new SwampMountainLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "swamp mountains";
    }
}
