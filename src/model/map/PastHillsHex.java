package model.map;

import view.MyColors;

public class PastHillsHex extends PastLandHex {
    public PastHillsHex(int rivers, int state) {
        super(new HillsHex(0, rivers, 0), MyColors.GREEN, 0, rivers, new HillsLocation(MyColors.GREEN), state);
    }
}
