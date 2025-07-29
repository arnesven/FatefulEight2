package model.map;

import view.MyColors;

public class PastMountainHex extends PastLandHex {
    public PastMountainHex(int rivers, int state) {
        super(new MountainHex(0, rivers, 0), MyColors.GREEN, 0, rivers, new MountainLocation(), state);
    }
}
