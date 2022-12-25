package model.map;

import view.MyColors;

public class TundraMountain extends TundraHex {
    public TundraMountain(int roads, int rivers) {
        super(roads, rivers, new MountainLocation());
    }

    @Override
    public String getTerrainName() {
        return "tundra mountains";
    }
}
