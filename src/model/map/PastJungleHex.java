package model.map;

import view.MyColors;

public class PastJungleHex extends PastLandHex {
    public PastJungleHex(int rivers, int state) {
        super(new JungleHex(0, rivers, state), MyColors.GREEN, 0, rivers, new JungleLocation(), state);
    }
}
