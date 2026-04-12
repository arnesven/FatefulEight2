package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class CaveExitMineObject extends MineObject {
    private static final Sprite SPRITE = new Sprite32x32("cavexitfrommine", "warehouse.png",
            0x27, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.RED, MyColors.BLACK);

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, SPRITE);
    }

    @Override
    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        state.exitThroughCaveOpening();
        return currentLocation;
    }
}
