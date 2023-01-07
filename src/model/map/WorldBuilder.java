package model.map;

import view.MyColors;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static model.map.WorldHex.NORTH;
import static model.map.WorldHex.SOUTH;
import static model.map.WorldHex.SOUTH_EAST;
import static model.map.WorldHex.SOUTH_WEST;
import static model.map.WorldHex.NORTH_WEST;
import static model.map.WorldHex.NORTH_EAST;



public class WorldBuilder {

    public static final int WORLD_WIDTH = 25;
    public static final int WORLD_HEIGHT = 18;

    private static String[] worldTemplate = new String[]{
            "TuTuttttsssttttTtTuTTTTTt",
            "TtwthTfpssspfttththuMTptw",
            "bwbwhhpfpssswwwwwfpMMwffw",
            "spbbhpspsssppwbwhfpMhwbfs",
            "ssspppsssssssphwhpMMhhbss",
            "psssssssssssshhhphhMhwwwh",
            "hhhsssssssppfspwwpphpwwwM",
            "hhhppfhpwwwfwppMMwMffwwhp",
            "phpfwwwwwhwwwfpppMffMwMMM",
            "pphpfwwhpppwpphpppMMdwddd",
            "pphffwwwwpwwphhhfhfdddddd",
            "MhffffwwwpwwwwwffpffDDpDf",
            "hhhppsphfpbbpwwfwfMfhfffp",
            "dDmDdmmppswwwppppwMMMMhpp",
            "ddddddmMpwwMwwwspppMMhfpf",
            "DdddDmMpwwdwpbbbffphMpffp",
            "DDpdpDddsdsddddddffhhpwff",
            "phfsspsssssssssssdfMMMwww"
    };

    private static Map<Point, HexContents> makeHexContents() {
        Map<Point, HexContents> contents = new HashMap<>();
        addRoadsAndRivers(contents,12, 0, SOUTH_WEST | SOUTH_EAST, 0);
        addRuins(contents, 14, 0, "Grond", SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,16, 0, SOUTH_WEST | SOUTH_EAST, 0);

        addTemple(contents, 1, 1, "Crystal");
        addTown(contents, 7, 1, "East Durham", "Mayor Bison", 0, 0);
        addTown(contents, 11, 1, "Cape Paxton", "Mayor Dargola", NORTH, 0);
        addRoadsAndRivers(contents, 13, 1, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 15, 1, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents,17, 1, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,18, 1, NORTH_WEST | SOUTH, 0);
        addCastle(contents, 22, 1, "Arkvale Castle", "Queen Valstine", MyColors.WHITE, SOUTH, 0);

        addTown(contents, 18, 2, "Urntown", "Elder Marten", NORTH | SOUTH, 0);
        addRoadsAndRivers(contents,22, 2, SOUTH_WEST | NORTH, 0);

        addCastle(contents, 1, 3, "Bogdown Castle", "King Burod", MyColors.DARK_GREEN, 0, 0);
        addRuins(contents, 5, 3, "Urh", 0, 0);
        addInn(contents, 12, 3, "Waterfront Inn", SOUTH_EAST, 0);
        addRoadsAndRivers(contents,18, 3, NORTH | NORTH_EAST, 0);
        addRoadsAndRivers(contents,19, 3, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,20, 3, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents,21, 3, SOUTH_WEST | NORTH_EAST, 0);

        addRoadsAndRivers(contents,13, 4, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,14, 4, NORTH_WEST | SOUTH, 0);


        addTown(contents, 0, 5, "Roukon", "Mayor Stephens", SOUTH_EAST, 0);
        addRoadsAndRivers(contents,14, 5, SOUTH | NORTH, 0);

        addRoadsAndRivers(contents, 1, 6, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 2, 6, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 14, 6, SOUTH | NORTH, SOUTH_WEST | SOUTH | NORTH_WEST);
        addTown(contents, 20, 6, "Ashtonshire", "Sheriff Alderborne", 0, 0);

        addRoadsAndRivers(contents, 0, 7, 0, SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 1, 7, 0, SOUTH);
        addRoadsAndRivers(contents, 2, 7, 0, SOUTH_WEST | SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 3, 7, NORTH_WEST | SOUTH_EAST, SOUTH | SOUTH_EAST);
        addTown(contents, 4, 7, "Ebonshire", "Lady Enid", NORTH_WEST, NORTH_WEST);
        addTown(contents, 13, 7, "Lower Theln", "Mayor Engels", 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 14, 7, NORTH_WEST | NORTH | SOUTH, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 15, 7, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 16, 7, 0, SOUTH);
        addTemple(contents, 24, 7, "the Peaks");

        addRoadsAndRivers(contents, 0, 8, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 1, 8, 0, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 2, 8, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 3, 8, 0, NORTH_WEST | NORTH | SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 4, 8, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 14, 8, NORTH | SOUTH_WEST, NORTH_EAST);
        addRoadsAndRivers(contents, 15, 8, 0, SOUTH_WEST | SOUTH | SOUTH_EAST | NORTH_WEST);
        addRoadsAndRivers(contents, 16, 8, 0, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 17, 8, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 18, 8, 0, SOUTH_WEST);

        addTemple(contents, 0, 9, "the Plains");
        addRoadsAndRivers(contents, 3, 9, 0, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 4, 9, 0, NORTH_WEST | SOUTH_WEST);
        addRoadsAndRivers(contents, 8, 9, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 9, 9, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 10, 9, NORTH_WEST | SOUTH_EAST, 0);
        addInn(contents,12,9, "Crossroads Inn", NORTH_EAST | SOUTH_EAST | SOUTH | SOUTH_WEST, 0);
        addRoadsAndRivers(contents, 13, 9, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 15, 9, 0, NORTH);
        addRoadsAndRivers(contents, 17, 9, 0, NORTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 18, 9, 0, SOUTH_WEST | NORTH_WEST);
        addRuins(contents, 24, 9, "Ronk", 0, 0);

        addRoadsAndRivers(contents, 3, 10, 0, NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 4, 10, 0, NORTH_WEST | SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 6, 10, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 7, 10, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 12, 10, NORTH | SOUTH, 0);
        addRoadsAndRivers(contents, 11, 10, NORTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 13, 10, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 14, 10, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 17, 10, 0, SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 18, 10, 0, SOUTH_WEST | NORTH_WEST);

        addRoadsAndRivers(contents, 2, 11, 0, SOUTH_EAST);
        addRoadsAndRivers(contents, 3, 11, 0, SOUTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 4, 11, SOUTH_WEST | NORTH_EAST, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 5, 11, SOUTH_WEST | NORTH_EAST, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 6, 11, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 12, 11, NORTH | SOUTH, 0);
        addRoadsAndRivers(contents, 15, 11, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 16, 11, NORTH_WEST | NORTH_EAST, 0);
        addTown(contents, 17, 11, "Upper Theln", "Mayor Rutherford", SOUTH_WEST, NORTH_EAST);
        addTown(contents, 22, 11, "Saint Quellin", "Councilman Egon", SOUTH_EAST, 0);

        addRoadsAndRivers(contents, 2, 12, 0, NORTH_EAST | SOUTH_EAST);
        addTown(contents, 3, 12, "Little Erinde", "Mayor Gorda", NORTH_EAST | SOUTH_EAST, SOUTH_WEST | NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 4, 12, NORTH_WEST | SOUTH, 0);
        addRoadsAndRivers(contents, 6, 12, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 7, 12, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 8, 12, 0, SOUTH_WEST | SOUTH);
        addTown(contents, 9, 12, "Ackerville", "Elder Treva", 0, 0);
        addRoadsAndRivers(contents, 12, 12, NORTH | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 23, 12, SOUTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 24, 12, SOUTH | NORTH_WEST, 0);

        addRoadsAndRivers(contents, 3, 13, 0, NORTH_WEST);
        addRoadsAndRivers(contents, 4, 13, NORTH | SOUTH_WEST, 0);
        addRoadsAndRivers(contents, 7, 13, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 8, 13, 0, NORTH);
        addRoadsAndRivers(contents, 13, 13, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 14, 13, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 15, 13, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 16, 13, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 24, 13, SOUTH_WEST | NORTH, 0);

        addRuins(contents, 0, 14, "Zand", 0, 0);
        addRoadsAndRivers(contents, 12, 14, 0, SOUTH);
        addRoadsAndRivers(contents, 16, 14, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 17, 14, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 18, 14, NORTH_WEST | SOUTH, 0);
        addRoadsAndRivers(contents, 22, 14, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 23, 14, SOUTH_WEST | NORTH_EAST, 0);

        addRoadsAndRivers(contents, 4, 15, SOUTH_WEST | SOUTH_EAST, 0);
        addInn(contents, 7, 15, "Hunter's Inn", SOUTH, 0);
        addTemple(contents, 10, 15, "the Surf");
        addTown(contents, 12, 15, "South Meadhome", "Mayor Calhoun", 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 13, 15, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 14, 15, 0, SOUTH_WEST | SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 15, 15, 0, SOUTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 16, 15, 0, NORTH_WEST);
        addTown(contents, 18, 15, "Sheffield", "Mayor Jorgensen", NORTH, 0);
        addCastle(contents, 21, 15, "Castle Ardh", "Lord Aldeck", MyColors.BLUE, NORTH_EAST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 22, 15, NORTH_WEST | SOUTH_EAST, 0);
        addTown(contents, 24, 15, "Bullsville", "Mayor Oppenberg", SOUTH_WEST, 0);

        addCastle(contents, 2, 16, "Sunblaze Castle", "Prince Elozi", MyColors.YELLOW, NORTH_EAST, 0);
        addRoadsAndRivers(contents, 3, 16, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 5, 16, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 6, 16, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 7, 16, SOUTH_WEST | NORTH, 0);
        addRoadsAndRivers(contents, 13, 16, 0, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 14, 16, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 15, 16, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 23, 16, NORTH_WEST | NORTH_EAST, 0);


        return contents;
    }

    private static void addRoadsAndRivers(Map<Point, HexContents> contents, int x, int y, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(null, roads, rivers));
    }

    private static void addRuins(Map<Point, HexContents> contents, int x, int y, String ruinsName, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new RuinsLocation(ruinsName), roads, rivers));
    }

    private static void addTemple(Map<Point, HexContents> contents, int x, int y, String templeName) {
        contents.put(new Point(x, y), new HexContents(new TempleLocation(templeName), 0, 0));
    }

    private static void addCastle(Map<Point, HexContents> contents, int x, int y, String castleName, String lordName, MyColors castleColor, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new CastleLocation(castleName, castleColor, lordName), roads, rivers));
    }

    private static void addInn(Map<Point, HexContents> contents, int x, int y, String innName, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new InnLocation(innName), roads, rivers));
    }

    private static void addTown(Map<Point, HexContents> contents, int x, int y, String townName, String lordName, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new TownLocation(townName, lordName, rivers != 0), roads, rivers));
    }


    public static WorldHex[][] buildWorld() {
        WorldHex[][] hexes = new WorldHex[WORLD_WIDTH][WORLD_HEIGHT];
        Map<Point, HexContents> hexContents = makeHexContents();
        for (int y = 0; y < worldTemplate.length; ++y) {
            for (int x = 0; x < WORLD_WIDTH; ++x) {
                HexContents contents = hexContents.get(new Point(x, y));
                hexes[x][y] = makeHex(worldTemplate[y].charAt(x), contents);
                if (contents != null && contents.location != null) {
                    contents.location.setHex(hexes[x][y]);
                }
            }
        }

        makeSeaBorders(hexes);

        return hexes;
    }

    private static void makeSeaBorders(WorldHex[][] hexes) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y] instanceof SeaHex) {
                    fixBorder(hexes, x, y+1, NORTH);
                    fixBorder(hexes, x, y-1, SOUTH);
                    if (x % 2 == 0) {
                        fixBorder(hexes, x + 1, y, SOUTH_WEST);
                        fixBorder(hexes, x - 1, y, SOUTH_EAST);
                        fixBorder(hexes, x + 1, y+1, NORTH_WEST);
                        fixBorder(hexes, x - 1, y+1, NORTH_EAST);
                    } else {
                        fixBorder(hexes, x + 1, y-1, SOUTH_WEST);
                        fixBorder(hexes, x - 1, y-1, SOUTH_EAST);
                        fixBorder(hexes, x + 1, y, NORTH_WEST);
                        fixBorder(hexes, x - 1, y, NORTH_EAST);
                    }
                }
            }
        }
    }

    private static void fixBorder(WorldHex[][] hexes, int x, int y, int direction) {
        if (0 <= x && x < WORLD_WIDTH) {
            if (0 <= y && y < WORLD_HEIGHT) {
                hexes[x][y].setRivers(hexes[x][y].getRivers() | direction);
            }
        }
    }

    private static WorldHex makeHex(char c, HexContents contents) {
        int roads = 0;
        int rivers = 0;
        HexLocation location = null;
        if (contents != null) {
            roads = contents.roads;;
            rivers = contents.rivers;
            location = contents.location;
        }
        if (c == 't') {
            return new TundraHex(roads, rivers, location);
        } else if (c == 'T') {
            return new TundraMountain(roads, rivers);
        } else if (c == 'u') {
            return new TundraHills(roads, rivers);
        } else if (c == 's') {
            return new SeaHex();
        } else if (c == 'w') {
            return new WoodsHex(roads, rivers);
        } else if (c == 'p') {
            return new PlainsHex(roads, rivers, location);
        } else if (c == 'b') {
            return new SwampHex(roads, rivers);
        } else if (c == 'M') {
            return new MountainHex(roads, rivers);
        } else if (c == 'm') {
            return new DesertMountain(roads, rivers);
        } else if (c == 'h') {
            return new HillsHex(roads, rivers);
        } else if (c == 'f') {
            return new FieldsHex(roads, rivers);
        } else if (c == 'd') {
            return new DesertHex(roads, rivers, location);
        } else if (c == 'D') {
            return new DesertHills(roads, rivers);
        }
        throw new IllegalStateException("No hex can be created for token '" + c + "'");
    }





    private static class HexContents {
        public HexLocation location;
        public int roads;
        public int rivers;

        public HexContents(HexLocation location, int roads, int rivers) {
            this.location = location;
            this.roads = roads;
            this.rivers = rivers;
        }
    }
}
