package model.map;

public class DeepWoodsHex extends WoodsHex {
    public DeepWoodsHex(int roads, int rivers, int state) {
        super(roads, rivers, new DeepWoodsLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "deep woods";
    }
}
