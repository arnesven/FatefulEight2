package model.map;

public class TundraWoods extends TundraHex {
    public TundraWoods(int roads, int rivers, int state) {
        super(roads, rivers, new WoodsLocation(true), state);
    }

    @Override
    public String getTerrainName() {
        return "forzen forest";
    }
}
