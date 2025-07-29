package model.map;

import view.MyColors;

public class PastSwampHex extends PastLandHex {
    public PastSwampHex(int rivers, int state) {
        super(new SwampHex(0, rivers, new SwampLocation(), 0),
                MyColors.GREEN, 0, rivers, new SwampLocation(), state);
    }
}
