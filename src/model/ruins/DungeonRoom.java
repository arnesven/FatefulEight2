package model.ruins;

import model.Model;
import model.quests.scenes.TrapSubScene;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DungeonRoom implements Serializable {

    public static final MyColors BRICK_COLOR = MyColors.GRAY;
    public static final MyColors FLOOR_COLOR = MyColors.GRAY_RED;
    public static final MyColors FLOOR_DETAIL_COLOR = MyColors.DARK_GRAY;

    private static final Sprite32x32 HORI_WALL = new Sprite32x32("horidungeonwall", "dungeon.png", 0x07,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 UL_CORNER = new Sprite32x32("dungeonul", "dungeon.png", 0x06,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 UR_CORNER = new Sprite32x32("dungeonur", "dungeon.png", 0x16,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 LR_CORNER = new Sprite32x32("dungeonlr", "dungeon.png", 0x25,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 LL_CORNER = new Sprite32x32("dungeonll", "dungeon.png", 0x15,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 T_WALL = new Sprite32x32("dungeontwall", "dungeon.png", 0x14,
            MyColors.BLACK, BRICK_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);

    private static final Sprite32x32 FLOOR = new Sprite32x32("dungeonfloor", "dungeon.png", 0x00,
            FLOOR_DETAIL_COLOR, FLOOR_DETAIL_COLOR, MyColors.BLACK, FLOOR_COLOR);
    private static final Sprite32x32 VERTI_WALL_LEFT = new Sprite32x32("leftvertidungeonwall", "dungeon.png", 0x05,
            MyColors.BLACK, MyColors.BLACK, FLOOR_COLOR, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_WALL_RIGHT = new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0x05,
            MyColors.BLACK, FLOOR_COLOR, MyColors.BLACK, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_WALL_BOTH = new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0x05,
            MyColors.BLACK, FLOOR_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);

    private static final Sprite32x32[] leftArray = new Sprite32x32[] {UL_CORNER, VERTI_WALL_LEFT, LL_CORNER};
    private static final Sprite32x32[] connect   = new Sprite32x32[] {T_WALL, VERTI_WALL_BOTH, HORI_WALL};
    private static final Sprite32x32[] rightArray = new Sprite32x32[] {UR_CORNER, VERTI_WALL_RIGHT, LR_CORNER};
    private static final Sprite32x32[] midArray = new Sprite32x32[] {HORI_WALL, FLOOR, HORI_WALL};

    private final ArrayList<DungeonObject> otherObjects;
    private final ArrayList<RoomDecoration> decorations;
    private final DungeonDoor[] connections = new DungeonDoor[4];
    private final int width;
    private final int height;
    private int cardinality;

    public DungeonRoom(int width, int height) {
        this.otherObjects = new ArrayList<>();
        decorations = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public DungeonRoom() {
        this(2, 2);
    }

    public void drawYourself(Model model, Point position, boolean connectLeft, boolean connectRight, boolean leftCorner, boolean rightCorner) {
        int xStart = position.x;
        int yStart = position.y;

        // TOP ROW
        Sprite corner = leftCorner ? T_WALL : (connectLeft ? connect[0] : leftArray[0]);
        model.getScreenHandler().put(xStart, yStart, corner);
        for (int w = 1; w < width+1; ++w) {
            model.getScreenHandler().put(xStart + 4 * w, yStart, midArray[0]);
        }
        corner = rightCorner ? T_WALL : (connectRight ? connect[0] : rightArray[0]);
        model.getScreenHandler().put(xStart + 4*(width+1), yStart, corner);

        // MIDDLE ROWS
        for (int yCurr = 1; yCurr < height+1; yCurr++) {
            corner = connectLeft ? connect[1] : leftArray[1];
            model.getScreenHandler().put(xStart, yStart + 4 * yCurr, corner);

            for (int w = 1; w < width+1; ++w) {
                model.getScreenHandler().put(xStart + 4 * w, yStart + 4 * yCurr, midArray[1]);
            }

            corner = connectRight ? connect[1] : rightArray[1];
            model.getScreenHandler().put(xStart + 4 * (width+1), yStart + 4 * yCurr, corner);
        }

        // BOTTOM ROW
        corner = (connectLeft ? connect[2] : leftArray[2]);
        model.getScreenHandler().put(xStart, yStart + 4 * (height+1), corner);
        for (int w = 1; w < width+1; ++w) {
            model.getScreenHandler().put(xStart + 4 * w, yStart + 4 * (height+1), midArray[2]);
        }
        corner = (connectRight ? connect[2] : rightArray[2]);
        model.getScreenHandler().put(xStart + 4*(width+1), yStart + 4 * (height+1), corner);


        for (RoomDecoration decor : decorations) {
            decor.drawYourself(model, xStart+decor.getInternalPosition().x*4, yStart+decor.getInternalPosition().y*4);
        }
    }

    public List<DungeonObject> getObjects() {
        List<DungeonObject> objs = new ArrayList<>(otherObjects);
        for (int i = 0; i < connections.length; ++i) {
            if (connections[i] != null) {
                objs.add(connections[i]);
            }
        }
        return objs;
    }

    public void setConnection(int index, DungeonDoor makeObject) {
        connections[index] = makeObject;
    }

    public void addObject(DungeonObject obj) {
        otherObjects.add(obj);
    }

    public void addDecoration(RoomDecoration obj) {
        decorations.add(obj);
    }

    public void setCardinality(int cardinalityCount) {
        this.cardinality = cardinalityCount;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        List<DungeonObject> objs = new ArrayList<>();
        objs.addAll(otherObjects);
        for (DungeonObject obj : objs) {
            obj.entryTrigger(model, exploreRuinsState);
        }
    }

    public void connectLeverToDoor(LeverObject lever) {
        for (int i = 0; i < 4; ++i) {
            if (connections[i] instanceof OpenDoor) {
                ((OpenDoor)connections[i]).addLeverConnection(lever);
                ((OpenDoor)connections[i].getLinkedDoor()).addLeverConnection(lever);
                break;
            }
        }
    }

    public void clearDecorations() {
        decorations.clear();
    }

    public Point getRelativeAvatarPosition() {
        return new Point(0, 0);
    }

    public void removeObject(DungeonObject dungeonMonster) {
        otherObjects.remove(dungeonMonster);
    }

    public int getMapRoomSpriteNumber() {
        int firstConnection = firstMissingConnection(true);
        int firstMissing = firstMissingConnection(false);
        switch (cardinality) {
            case 0:
                return 0xF0;
            case 1:
                return 0xC3 + firstConnection;
            case 2:
                if (connections[(firstConnection + 2) % 4] != null) {
                    return 0xD7 + firstConnection;
                } else {
                    if (connections[(firstConnection + 1)] != null) {
                        return 0xD3 + firstConnection;
                    } else {
                        return 0xD6;
                    }
                }
            case 3:
                return 0xE3 + firstMissing;
        }
        // cardinality == 4
        return 0xE7;
    }

    private int firstMissingConnection(boolean negate) {
        for (int i = 0; i < connections.length; ++i) {
            if ((connections[i] == null) == !negate) {
                return i;
            }
        }
        return -1;
    }

    public int getMapExtraIconSpriteNumber() {
        if (connections[0] instanceof StairsUp) {
            return 0xF3;
        }
        if (connections[0] instanceof StairsDown) {
            return 0xF4;
        }
        for (DungeonObject objs : otherObjects) {
            if (objs instanceof LeverObject) {
                return 0xF5;
            } else if (objs instanceof DungeonTrap) {
                return 0xF6;
            } else if (objs instanceof DungeonMonster) {
                return 0xF7;
            }
        }

        return 0;
    }
}
