package model.map.locations;

import model.map.MountainLocation;
import model.map.SwampLocation;
import view.ScreenHandler;

public class SwampMountainLocation extends SwampLocation {
    private final MountainLocation mountLoc;

    public SwampMountainLocation() {
        this.mountLoc = new MountainLocation();
    }

    @Override
    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        super.drawUpperHalf(screenHandler, x, y, flag);
        mountLoc.drawUpperHalf(screenHandler, x, y, flag);
    }

    @Override
    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        super.drawLowerHalf(screenHandler, x, y);
        mountLoc.drawLowerHalf(screenHandler, x, y);
    }
}
