package model.map;

import view.MyColors;

public class PastDeepWoodsHex extends PastLandHex {
    public PastDeepWoodsHex(int rivers, int state) {
        super(new DeepWoodsHex(0, rivers, 0), MyColors.GREEN, 0, rivers, new DeepWoodsLocation(), state);
    }
}
