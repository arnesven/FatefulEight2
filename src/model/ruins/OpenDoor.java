package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static model.ruins.DungeonRoom.BRICK_COLOR;
import static model.ruins.DungeonRoom.FLOOR_COLOR;

public class OpenDoor extends DungeonDoor {

    private static final Sprite32x32 HORI_DOOR = new Sprite32x32("horidoor", "dungeon.png", 0x12,
            MyColors.BLACK, BRICK_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);
    private static final Sprite32x32 HORI_LEVER_DOOR = new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
            MyColors.BLACK, MyColors.PINK, MyColors.RED, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_LEVER_DOOR = new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
            MyColors.RED, MyColors.PINK, MyColors.DARK_RED, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_DOOR = new Sprite32x32("vertidoor", "dungeon.png", 0x22,
            MyColors.BLACK, BRICK_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);
    private static final Sprite32x32 DOOR_OVERLAY = new Sprite32x32("dooroverlay", "dungeon.png", 0x30,
            MyColors.BLACK, BRICK_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);
    private final Sprite32x32 sprite;
    private final String direction;
    private final boolean isHorizontal;
    private List<LeverObject> levers = new ArrayList<>();

    public OpenDoor(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.isHorizontal = isHorizontal;
        this.sprite = isHorizontal ? HORI_DOOR : VERTI_DOOR;
        this.direction = direction;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        super.drawYourself(model, xPos, yPos);
        if (isHorizontal) {
            model.getScreenHandler().register(DOOR_OVERLAY.getName(), new Point(xPos, yPos), DOOR_OVERLAY, 3);
        }
        if (!levers.isEmpty() && leversWrong()) {
            if (isHorizontal) {
                model.getScreenHandler().register(HORI_LEVER_DOOR.getName(), new Point(xPos, yPos), HORI_LEVER_DOOR, 1);
            } else {
                model.getScreenHandler().register(VERTI_LEVER_DOOR.getName(), new Point(xPos, yPos), VERTI_LEVER_DOOR, 1);
            }
        }
    }

    private boolean leversWrong() {
        for (LeverObject lever : levers) {
            if (!lever.isOn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "A door going " + this.direction;
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (!levers.isEmpty() && leversWrong()) {
            state.println("This door's lock cannot be picked.");
        } else {
            if (direction.equals("north")) {
                state.goDirection(model, new Point(0, -1));
            } else if (direction.equals("south")) {
                state.goDirection(model, new Point(0, 1));
            } else if (direction.equals("east")) {
                state.goDirection(model, new Point(1, 0));
            } else {
                state.goDirection(model, new Point(-1, 0));
            }
        }
    }

    public void addLeverConnection(LeverObject lever) {
        this.levers.add(lever);
    }
}
