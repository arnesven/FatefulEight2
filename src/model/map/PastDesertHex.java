package model.map;

import view.MyColors;

public class PastDesertHex extends PastLandHex {
    public PastDesertHex(int rivers, int state, HexLocation hexLocation) {
        super(new DesertHex(0, rivers, hexLocation, 0),
                MyColors.YELLOW, 0, rivers, hexLocation, state);
    }
}
