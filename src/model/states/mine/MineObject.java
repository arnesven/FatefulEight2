package model.states.mine;

import model.Model;
import view.ScreenHandler;

import java.awt.*;

public abstract class MineObject {
    public abstract void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition);

    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return currentLocation;
    }

    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return true;
    }
}
