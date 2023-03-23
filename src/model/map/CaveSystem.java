package model.map;

import util.MyRandom;
import view.ScreenHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CaveSystem extends World {
    private static List<Integer> tunnels =
            List.of(Direction.NORTH_WEST, Direction.NORTH, Direction.NORTH_EAST,
                    Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST);

    public CaveSystem(World overWorld) {
        super(makeHexes(overWorld));
    }

    private static WorldHex[][] makeHexes(World overWorld) {
        WorldHex[][] hexes = new WorldHex[WorldBuilder.WORLD_WIDTH][WorldBuilder.WORLD_HEIGHT];
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                if (overWorld.getHex(new Point(x, y)) instanceof SeaHex) {
                    hexes[x][y] = new SolidRockHex();
                } else {
                    hexes[x][y] = new CaveHex(Direction.NONE);
                }
            }
        }
        makeTunnels(hexes);
        return hexes;
    }

    private static void makeTunnels(WorldHex[][] hexes) {
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                if (!(hexes[x][y] instanceof SolidRockHex)) {
                    List<Point> dirs = new ArrayList<>(Direction.getDxDyDirections(new Point(x, y)));
                    for (int i = 0; i < MyRandom.randInt(2, 3); ++i) {
                        Point randDir = dirs.remove(MyRandom.randInt(dirs.size()));
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
                           Point partyPosition, int mapYRange, int yOffset) {
        List<Point> list = Direction.getDxDyDirections(partyPosition);
        Point diff = new Point(x - partyPosition.x, y - partyPosition.y);
        boolean ok = (x == partyPosition.x && y == partyPosition.y);
        for (Point p : list) {
            if (p.x == diff.x && p.y == diff.y) {
                ok = true;
            }
        }
        if (!ok) {
            return;
        }
        super.drawHex(screenHandler, x, y, screenX, screenY, partyPosition, mapYRange, yOffset);
    }
}
