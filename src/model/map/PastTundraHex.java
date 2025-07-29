package model.map;

import view.MyColors;

public class PastTundraHex extends PastLandHex {
    public PastTundraHex(int rivers, int state, HexLocation hexLocation) {
        super(new TundraHex(0, rivers, hexLocation, 0),
                MyColors.WHITE, 0, rivers, hexLocation, state);
    }
}
