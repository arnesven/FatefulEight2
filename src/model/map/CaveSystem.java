package model.map;

import model.Model;
import model.map.locations.FortressAtUtmostEdgeLocation;
import model.map.objects.MapObject;
import util.MyRandom;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static model.map.Direction.NORTH;
import static model.map.Direction.SOUTH;

public class CaveSystem extends World {
    private static final String VISITED_KEY = "caveHexVisited";
    private static List<Integer> tunnels =
            List.of(Direction.NORTH_WEST, NORTH, Direction.NORTH_EAST,
                    Direction.SOUTH_EAST, SOUTH, Direction.SOUTH_WEST);

    public CaveSystem(World overWorld, int seed) {
        super(makeHexes(overWorld, seed), WorldBuilder.getWorldBounds(overWorld.getCurrentState()));
    }

    private static WorldHex[][] makeHexes(World overWorld, int seed) {
        Random random = new Random(seed);
        CaveHex.setRandom(random);
        Rectangle worldBounds = WorldBuilder.getWorldBounds(overWorld.getCurrentState());
        Point fortressPosition = new Point(
                worldBounds.x + 1 + random.nextInt(worldBounds.width-1),
                worldBounds.y + 1 + random.nextInt(worldBounds.height-1));
        System.out.println("FatUE is at x=" + fortressPosition.x + ", y=" + fortressPosition.y);

        WorldHex[][] hexes = new WorldHex[WorldBuilder.WORLD_WIDTH][WorldBuilder.WORLD_HEIGHT];
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                int state = WorldBuilder.getStateForXY(x, y);
                if (fortressPosition.x == x && fortressPosition.y == y) {
                    hexes[x][y] = new FortressCaveHex(state);
                } else if (noExit(overWorld.getHex(new Point(x, y)))) {
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
                if (hexes[x][y] instanceof FortressCaveHex) {
                    hexes[x][y].setRoads(SOUTH | hexes[x][y].getRoads());
                    hexes[x][y+1].setRoads(NORTH | hexes[x][y+1].getRoads());
                } else {
                    List<Point> dirs = new ArrayList<>(Direction.getDxDyDirections(new Point(x, y)));
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        Point randDir = dirs.remove(random.nextInt(dirs.size()));
                        int otherX = x + randDir.x;
                        int otherY = y + randDir.y;
                        if (coordinatesOK(otherX, otherY) && !(hexes[otherX][otherY] instanceof FortressCaveHex)) {
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
    protected void drawHex(Model model, int x, int y, int screenX, int screenY,
                           Point partyPosition, int mapYRange, int yOffset, int flag, List<MapObject> mapObjects) {
        boolean isPartyPos = (x == partyPosition.x && y == partyPosition.y);
        List<Point> list = Direction.getDxDyDirections(partyPosition);
        Point diff = new Point(x - partyPosition.x, y - partyPosition.y);
        boolean isAdjacent = list.contains(diff) && model.isInCaveSystem();
        boolean hasTunnel = isAdjacent &&
                getHex(partyPosition).getRoadInDirection(Direction.getDirectionForDxDy(partyPosition, diff));

        if (isPartyPos || previouslyVisited(model, x, y) || hasTunnel) {
            super.drawHex(model, x, y, screenX, screenY, partyPosition,
                    mapYRange, yOffset, HexLocation.FLAG_NONE, mapObjects);
        }
    }

    private boolean previouslyVisited(Model model, int x, int y) {
        return model.getSettings().getMiscFlags().containsKey(VISITED_KEY + x + "-" + y);
    }


    public static void visitPosition(Model model, Point position) {
        model.getSettings().getMiscFlags().put(VISITED_KEY + position.x + "-" + position.y, true);
    }

    public Point getFatuePosition() {
        for (int y = 0; y < WorldBuilder.WORLD_HEIGHT; ++y) {
            for (int x = 0; x < WorldBuilder.WORLD_WIDTH; ++x) {
                WorldHex hex = getHex(new Point(x, y));
                if (hex.getLocation() instanceof FortressAtUtmostEdgeLocation) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}
