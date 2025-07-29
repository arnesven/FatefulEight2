package model.map;

import view.MyColors;

public class PastTundraWoodsHex extends PastLandHex {
    public PastTundraWoodsHex(int rivers, int state) {
        super(new TundraWoods(0, rivers, 0),
                MyColors.WHITE, 0, rivers, new WoodsLocation(true), state);
    }
}
