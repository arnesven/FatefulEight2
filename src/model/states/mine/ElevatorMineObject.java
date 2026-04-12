package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.util.ArrayList;

public class ElevatorMineObject extends MineObject {
    private static final Sprite SPRITE = new Sprite32x32("elevator",
            "warehouse.png", 0x36, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.GRAY, MyColors.WHITE);
    private static final Sprite SHAFT = new Sprite32x32("shaft",
            "warehouse.png", 0x26, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.WHITE);
    private MineRoomLocation room;
    private Point positionWithinRoom;

    public ElevatorMineObject(MineRoomLocation location, Point pointInRoom) {
        room = location;
        positionWithinRoom = pointInRoom;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        if (mine.getCurrentLocation().level == room.level) {
            screenHandler.put(screenPosition.x, screenPosition.y, SPRITE);
        } else {
            screenHandler.put(screenPosition.x, screenPosition.y, SHAFT);
        }
    }

    @Override
    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        if (state.getDiscoveredLevels(model) < 2) {
            state.println("This is some kind of elevator, but you can't make sense of the controls.");
            return currentLocation;
        }
        int destinationLevel = state.askToTakeElevator(model);
        if (destinationLevel > 0) {
            return state.moveWithElevator(model, state, destinationLevel);
        }
        return currentLocation;
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        boolean elevatorIsOnLevel = state.getCurrentLocation().level == room.level;
        if (!elevatorIsOnLevel) {
            state.leaderSay("A shaft with a cable going down into the abyss... Is there an elevator in the mine?");
        }
        return elevatorIsOnLevel;
    }

    public void setLevel(int level) {
        this.room.level = level;
    }

    public Point getPositionInRoom() {
        return positionWithinRoom;
    }

    public MineRoomLocation getLocation() {
        return room;
    }
}
