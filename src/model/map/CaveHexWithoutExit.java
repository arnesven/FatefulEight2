package model.map;

import view.MyColors;


public class CaveHexWithoutExit extends CaveHex {
    public CaveHexWithoutExit(int tunnels) {
        super(tunnels, MyColors.GRAY);
    }

    @Override
    protected boolean canHaveExit() {
        return false;
    }
}
