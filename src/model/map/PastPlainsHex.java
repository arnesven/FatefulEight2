package model.map;

import view.MyColors;

public class PastPlainsHex extends PastLandHex {
    public PastPlainsHex(int rivers, int state, HexLocation loc) {
        super(new PlainsHex(0, rivers, loc, 0),
                MyColors.GREEN, 0, rivers, loc, state);
    }
}
