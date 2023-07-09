package model.map;

public class JungleHex extends WoodsHex {
    public JungleHex(int roads, int rivers, int state) {
        super(roads, rivers, new JungleLocation(), state);
    }


    @Override
    public String getTerrainName() {
        return "jungle";
    }
}
