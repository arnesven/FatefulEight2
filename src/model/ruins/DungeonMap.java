package model.ruins;

import model.Model;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.subviews.SubView;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DungeonMap implements Serializable {
    private static final MyColors MAP_BACKGROUND = MyColors.BEIGE;
    private static final MyColors MAP_FOREGROUND = MyColors.BROWN;

    private static final Sprite[][] MAP_BORDERS = makeBorderSprites();
    private static final Sprite NO_ROOM_SPRITE = MAP_BORDERS[1][1];
    private static final Map<Integer, Sprite> roomIconCache = new HashMap<>();

    private static final int FIXED_Y_OFFSET = 4;
    private static final Sprite PARTY_ICON = new PartyIconSprite();

    private final RuinsDungeon dungeon;

    public DungeonMap(RuinsDungeon ruinsDungeon) {
        this.dungeon = ruinsDungeon;
    }

    public void drawYourself(Model model, Point partyPosition, int currentLevel) {
        DungeonLevel level = dungeon.getLevel(currentLevel);
        DungeonRoom[][] rooms = level.getRooms();
        int xOff = 8 - rooms.length/2;
        int yOff = 8 - rooms[0].length/2;
        Point clearStart = convertToScreen(xOff-1, yOff-1);
        Point clearEnd = convertToScreen(xOff + rooms.length+1, yOff + rooms[0].length+1);
        model.getScreenHandler().clearForeground(clearStart.x, clearEnd.x, clearStart.y, clearEnd.y);
        drawBordersAndRooms(model, xOff, yOff, rooms, partyPosition);

        Point currentPos = convertToScreen(xOff + partyPosition.x, yOff + partyPosition.y);
        model.getScreenHandler().register(PARTY_ICON.getName(), currentPos, PARTY_ICON);
    }

    private void drawBordersAndRooms(Model model, int xOff, int yOff, DungeonRoom[][] rooms, Point partyPosition) {
        // top row
        drawSpriteAt(model, xOff-1, yOff-1, MAP_BORDERS[0][0]);
        for (int x = 0; x < rooms[0].length; ++x) {
            drawSpriteAt(model, xOff+x, yOff-1, MAP_BORDERS[1][0]);
        }
        drawSpriteAt(model, xOff+rooms.length, yOff-1, MAP_BORDERS[2][0]);

        for (int y = 0; y < rooms[0].length; ++y) {
            drawSpriteAt(model, xOff-1, yOff+y, MAP_BORDERS[0][1]);
            for (int x = 0; x < rooms.length; ++x) {
                if (rooms[x][y] == null || !rooms[x][y].isRevealedOnMap()) {
                    drawSpriteAt(model, xOff+x, yOff+y, NO_ROOM_SPRITE);
                } else {
                    Sprite spriteToDraw = getRoomIconSprite(rooms[x][y].getMapRoomSpriteNumber());
                    drawSpriteAt(model, xOff+x, yOff+y, spriteToDraw);
                    int extraIcon = rooms[x][y].getMapExtraIconSpriteNumber();
                    if (extraIcon != 0) {
                        Sprite sp2 = getRoomIconSprite(extraIcon);
                        Point p = convertToScreen(xOff + x, yOff + y);
                        model.getScreenHandler().register(sp2.getName(), p, sp2, 1);
                    }
                }
            }
            drawSpriteAt(model, xOff+rooms.length, yOff+y, MAP_BORDERS[2][1]);
        }

        // bottom
        drawSpriteAt(model, xOff-1, yOff+rooms[0].length, MAP_BORDERS[0][2]);
        for (int x = 0; x < rooms[0].length; ++x) {
            drawSpriteAt(model, xOff+x, yOff+rooms[0].length, MAP_BORDERS[1][2]);
        }
        drawSpriteAt(model, xOff+rooms.length, yOff+rooms[0].length, MAP_BORDERS[2][2]);
    }

    private Sprite getRoomIconSprite(int mapRoomSpriteNumber) {
        if (!roomIconCache.containsKey(mapRoomSpriteNumber)) {
            roomIconCache.put(mapRoomSpriteNumber, makeMapSprite(mapRoomSpriteNumber));
        }
        return roomIconCache.get(mapRoomSpriteNumber);
    }

    private void drawSpriteAt(Model model, int x, int y, Sprite sprite) {
        Point toScreen = convertToScreen(x, y);
        model.getScreenHandler().put(toScreen.x, toScreen.y, sprite);
    }

    private Point convertToScreen(int x, int y) {
        return new Point(SubView.X_OFFSET + x * 2, FIXED_Y_OFFSET + SubView.Y_OFFSET + y * 2);
    }


    private static Sprite makeMapSprite(int num) {
        return new Sprite16x16("mapnoroom", "dungeon.png", num,
                MAP_BACKGROUND, MAP_FOREGROUND, MyColors.RED, MyColors.BEIGE);
    }


    private static Sprite[][] makeBorderSprites() {
        Sprite[][] result = new Sprite16x16[3][3];
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                result[x][y] = makeMapSprite(0xC0 + 0x10*y + x);
            }
        }
        return result;
    }

    private static class PartyIconSprite extends LoopingSprite {
        public PartyIconSprite() {
            super("mappartyicon", "dungeon.png", 0xF1, 16);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.RED);
            setFrames(2);
            setDelay(32);
        }
    }
}
