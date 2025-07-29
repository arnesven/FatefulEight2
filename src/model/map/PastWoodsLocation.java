package model.map;

import view.MyColors;

public class PastWoodsLocation extends PastLandHex {
    public PastWoodsLocation(int rivers, int state) {
        super(new WoodsHex(0, rivers, new WoodsLocation(false), 0),
                MyColors.GREEN, 0, rivers, new WoodsLocation(false), state);
    }
}
