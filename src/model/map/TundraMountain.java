package model.map;

import java.awt.*;

public class TundraMountain extends TundraHex {
    public TundraMountain(int roads, int rivers, int state) {
        super(roads, rivers, new MountainLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "tundra mountains";
    }

    @Override
    public WorldHex makePastSelf(Point position) {
        if (position.y >= 6) {
            return new PastMountainHex(getRivers(), getState());
        }
        return super.makePastSelf(position);
    }
}
