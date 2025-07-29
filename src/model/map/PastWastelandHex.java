package model.map;

import view.MyColors;

public class PastWastelandHex extends PastLandHex {
    public PastWastelandHex(int rivers, int state, HexLocation hexLocation) {
        super(new WastelandHex(0, rivers, hexLocation, 0),
                MyColors.TAN, 0, rivers, hexLocation, state);
    }
}
