package model.ruins;

import model.Model;
import model.ruins.objects.*;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DungeonRoom implements Serializable {

    private final ArrayList<DungeonObject> otherObjects;
    private final ArrayList<RoomDecoration> decorations;
    private final DungeonDoor[] connections = new DungeonDoor[4];
    private final int width;
    private final int height;
    private int cardinality;
    private boolean revealedOnMap = false;

    public DungeonRoom(int width, int height) {
        this.otherObjects = new ArrayList<>();
        decorations = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public DungeonRoom() {
        this(2, 2);
    }

    public void drawYourself(Model model, Point position, boolean connectLeft, boolean connectRight,
                             boolean leftCorner, boolean rightCorner, DungeonTheme theme) {
        int xStart = position.x;
        int yStart = position.y;

        // TOP ROW
        Sprite corner = leftCorner ? theme.getConnect()[0] : (connectLeft ? theme.getConnect()[0] : theme.getLeft()[0]);
        model.getScreenHandler().put(xStart, yStart, corner);
        for (int w = 1; w < width+1; ++w) {
            model.getScreenHandler().put(xStart + 4 * w, yStart, theme.getMid()[0]);
        }
        corner = rightCorner ? theme.getConnect()[0] : (connectRight ? theme.getConnect()[0] : theme.getRight()[0]);
        model.getScreenHandler().put(xStart + 4*(width+1), yStart, corner);

        // MIDDLE ROWS
        for (int yCurr = 1; yCurr < height+1; yCurr++) {
            corner = connectLeft ? theme.getConnect()[1] : theme.getLeft()[1];
            model.getScreenHandler().put(xStart, yStart + 4 * yCurr, corner);

            for (int w = 1; w < width+1; ++w) {
                model.getScreenHandler().put(xStart + 4 * w, yStart + 4 * yCurr, theme.getMid()[1]);
            }

            corner = connectRight ? theme.getConnect()[1] : theme.getRight()[1];
            model.getScreenHandler().put(xStart + 4 * (width+1), yStart + 4 * yCurr, corner);
        }

        // BOTTOM ROW
        corner = (connectLeft ? theme.getConnect()[2] : theme.getLeft()[2]);
        model.getScreenHandler().put(xStart, yStart + 4 * (height+1), corner);
        for (int w = 1; w < width+1; ++w) {
            model.getScreenHandler().put(xStart + 4 * w, yStart + 4 * (height+1), theme.getMid()[2]);
        }
        corner = (connectRight ? theme.getConnect()[2] : theme.getRight()[2]);
        model.getScreenHandler().put(xStart + 4*(width+1), yStart + 4 * (height+1), corner);


        for (RoomDecoration decor : decorations) {
            decor.drawYourself(model, xStart+decor.getInternalPosition().x*4, yStart+decor.getInternalPosition().y*4, theme);
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
        revealedOnMap = true;
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
                if (!connectionBlocked((firstConnection + 2) % 4)) { // TODO : or cracked wall
                    return 0xD7 + firstConnection;
                } else {
                    if (!connectionBlocked(firstConnection + 1)) { // TODO: or cracked wall
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
            if (connectionBlocked(i) == !negate) {
                return i;
            }
        }
        return -1;
    }

    private boolean connectionBlocked(int i) {
        return connections[i] == null || connections[i] instanceof CrackedWall;
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

    public boolean isRevealedOnMap() {
        return revealedOnMap;
    }

    public void setRevealedOnMap(boolean b) {
        revealedOnMap = b;
    }

    public DungeonDoor getConnection(int index) {
        return connections[index];
    }
}
