package model.map;

public class CaveHexWithoutExit extends CaveHex {
    public CaveHexWithoutExit(int tunnels) {
        super(tunnels);
    }

    @Override
    protected boolean canHaveExit() {
        return false;
    }
}
