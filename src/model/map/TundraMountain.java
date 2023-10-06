package model.map;

public class TundraMountain extends TundraHex {
    public TundraMountain(int roads, int rivers, int state) {
        super(roads, rivers, new MountainLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "tundra mountains";
    }
}
