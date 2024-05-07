package model.map;

import model.map.objects.MapObject;
import util.MyRandom;
import view.ScreenHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveSystem extends World {
    private static List<Integer> tunnels =
            List.of(Direction.NORTH_WEST, Direction.NORTH, Direction.NORTH_EAST,
                    Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST);

    public CaveSystem(World overWorld, int seed) {
        super(makeHexes(overWorld, seed));
    }

    private static WorldHex[][] makeHexes(World overWorld, int seed) {
        Random random = new Random(seed);
        CaveHex.setRandom(random);
        WorldHex[][] hexes = new WorldHex[WorldBuilder.WORLD_WIDTH][WorldBuilder.WORLD_HEIGHT];
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                int state = WorldBuilder.getStateForXY(x, y);
                if (noExit(overWorld.getHex(new Point(x, y)))) {
                    hexes[x][y] = new CaveHexWithoutExit(Direction.NONE, state);
                } else {
                    hexes[x][y] = new CaveHex(Direction.NONE, state);
                }
            }
        }
        makeTunnels(hexes, random);
        return hexes;
    }

    private static boolean noExit(WorldHex hex) {
        if (hex instanceof SeaHex) {
            return true;
        }
        if (hex.getLocation() != null && !hex.getLocation().isDecoration()) {
            return true;
        }
        return false;
    }

    private static void makeTunnels(WorldHex[][] hexes, Random random) {
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                if (!(hexes[x][y] instanceof SolidRockHex)) {
                    List<Point> dirs = new ArrayList<>(Direction.getDxDyDirections(new Point(x, y)));
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        Point randDir = dirs.remove(random.nextInt(dirs.size()));
                        int otherX = x + randDir.x;
                        int otherY = y + randDir.y;
                        if (coordinatesOK(otherX, otherY) && !(hexes[otherX][otherY] instanceof SolidRockHex)) {
                            int tunnel = Direction.getDirectionForDxDy(new Point(x, y), randDir);
                            hexes[x][y].setRoads(tunnel | hexes[x][y].getRoads());
                            int opTunnel = Direction.opposite(Direction.getDirectionForDxDy(new Point(x, y), randDir));
                            hexes[otherX][otherY].setRoads(opTunnel | hexes[otherX][otherY].getRoads());
                        }
                    }
                }
            }
        }

    }

    private static boolean coordinatesOK(int otherX, int otherY) {
        return 0 <= otherX && otherX < WorldBuilder.WORLD_WIDTH && 0 <= otherY && otherY < WorldBuilder.WORLD_HEIGHT;
    }

    @Override
    protected void drawHex(ScreenHandler screenHandler, int x, int y, int screenX, int screenY,
                           Point partyPosition, int mapYRange, int yOffset, int flag, List<MapObject> mapObjects) {
        boolean isPartyPos = (x == partyPosition.x && y == partyPosition.y);
        List<Point> list = Direction.getDxDyDirections(partyPosition);
        Point diff = new Point(x - partyPosition.x, y - partyPosition.y);
        boolean isAdjacent = list.contains(diff);

        if (isAdjacent) {
            if (!getHex(partyPosition).getRoadInDirection(Direction.getDirectionForDxDy(partyPosition, diff))) {
                return;
            }
        } else if (!isPartyPos) {
            return;
        }
        super.drawHex(screenHandler, x, y, screenX, screenY, partyPosition,
                mapYRange, yOffset, HexLocation.FLAG_NONE, mapObjects);
    }
}
