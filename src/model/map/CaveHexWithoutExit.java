package model.map;

public class CaveHexWithoutExit extends CaveHex {
    public CaveHexWithoutExit(int tunnels, int state) {
        super(tunnels, state);
    }

    @Override
    protected boolean canHaveExit() {
        return false;
    }
}
