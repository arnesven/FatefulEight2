package model.map;

import model.map.locations.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static model.map.Direction.*;

public class WorldBuilder {

    public static final int WORLD_WIDTH = 53;
    public static final int WORLD_HEIGHT = 38;
    private static final int EXTRA_WIDTH = 14;
    private static final int EXTRA_HEIGHT = 10;
    public static final int ORIGINAL = 0;
    public static final int EXPAND_EAST = 1;
    public static final int EXPAND_SOUTH = 2;
    public static final int EXPAND_WEST = 4;
    public static final int EXPAND_NORTH = 8;

    private static String[] worldTemplate = new String[]{
            "ssssstTTTTTTTTTTTTTTTTttTTTTTTTTTTTuuTTTTTTTTTTTTTTTT",
            "sssssttuuTTTTTTuuuTTTTTTTuuTTTTTTTtTTTTTttTTTTTTTTTtt",
            "sttstttuTTTTTTTuuuuuuuuuuTTTTuuuuutuTTTuuTTuuTTuuuTuu",
            "stssttttWWWWWTTWWWWWWWWWTTWWWWWWWTTTTWWWWWWWWWWWWWWWW",
            "sssssttWWWWWWWTTTWWWWWWWWWWWWTTTTWTTuWWWWWWWWWWWWWWWW",
            "sssssssttttWWWWuTTTttttTtTTTTuttuTTTuttWWtttssWWutttt",
            "ssstsssssttttWWWuuuTTTTTTtttttuTTTuttttttssssssWWuWtt",
            "sssssssssstttWWWWWuWuTTtsstttuTTTTutWWttttssstsWWtttt",
            "sssssssstttWWTTWWTWWTTtsssstTTtuTTTtTuttssssssttttttt",
            "ssttttttWWuTuWWWTTTtttssssttTTtttTTTttttttstttttttttt",
            "ssssstttttTuWuTuTuttttsssttttTtTuTTTTTtttsstWuWuuWWuu",
            "ssttssttttttttTtwthTfpssspfttththuMTptwttsWWWWWWXXWWW",
            "sttttsstttttttbwbwhhpfpssswwwwwfpMMwffwtssXXXXxxXxXXX",
            "stppptssppppbbspbbhpspsssppwbwhfpMhwbfssssXXXXXXXXXXX",
            "pppppsspppppssssspppsssssssphwhpMMhhbsssssssXXXXXXxXX",
            "ssppppsssppsshpsssssssssssshhhphhMhwwwwbssssXXXXxxxXX",
            "ssspppsssssshhhhhsssssssppfspwwpphpwwwMMXXXXXXXXXXXXX",
            "spppsspppsphhphhhppfhpwwwfwppMMwMffwwhpXXXXxxXXbXpxxX",
            "sssspppppssphpphpfwwwwwhwwwfpppMffMwMMMXddXXXXxXXdDDd",
            "sspppffppssspppphpfwwhpppwpphpppMMdwddddXdDDDXXMXXXDd",
            "ssppfMMpppsspppphffwwwwpwwphhhfhfdddddddDddXXXMbXXXXX",
            "ssppfMhpppssMMMhffffwwwpwwwwwffpffDDpDXXXXXXXbMsbXXpX",
            "sspfhhpsssppMhhhhppsphfpbbpwwfwfMfhfffpwowXbbbbbMxxXX",
            "ssspppssppddmDdDmDdmmppswwwppppwMMMMhppppwwbbbbooMMxM",
            "sssssssdDdDmdDddddddmMpwwMwwwspppMMhfpffwwoobboooooMb",
            "ssphsddddDmdddDdddDmMpwwdwpbbbffphMpffpfwooooooooooww",
            "ssppppddmDDDddDDpdpDddsdsddddddffhhpwfffoooooooooowss",
            "ssppppddDDDDphphfsspsssssssssssdfMMMwwwooooooooowwsss",
            "ssssssssssssssssssssssssssssssssMMwwwwwwsssssssssssss",
            "sssssssssssssssssssssssssssssssMMwwwwwwwsssssssssssss",
            "ssssssssssssssssssssssssssssssMhMwwwwswwsssssssssssss",
            "ssssssssssssssssssssssssssssMMhMhwhwhwwwsssssssssssss",
            "sssssssssssssssssssssssssssMhhwwhjjwhwwwsssssssssssss",
            "jjjjjjjjjjjjjjjjjjjjjjjMMjjMMjjjjwjjwjjjjjjjjjjjjjjjj",
            "jjjjjjjjjjjjjjjjjjjjjMMMMMjjjjjjjjjwjjjjjjjjjjjjjjjjj",
            "jjjjjjjjjjjjjjjjjjjjjjjjMjjjjjjjjjjjjjjjjjjjjjjjjjjjj",
            "jjjjjjjjjjjjjjjjjjjjjjjjjMMjjjjjjjjjjjjjjjjjjjjjjjjjj",
            "jjjjjjjjjjjjjjjjjpjjjjjjjjjMMjjjjjjjjjjjjjjjjjjjjjjjj",
    };

    private static WorldHex makeHex(char c, HexContents contents, int state) {
        int roads = 0;
        int rivers = 0;
        HexLocation location = null;
        if (contents != null) {
            roads = contents.roads;;
            rivers = contents.rivers;
            location = contents.location;
        }
        if (c == 't') {
            return new TundraHex(roads, rivers, location, state);
        } else if (c == 'T') {
            return new TundraMountain(roads, rivers, state);
        } else if (c == 'u') {
            return new TundraHills(roads, rivers, state);
        } else if (c == 'W') {
            return new TundraWoods(roads, rivers, state);
        } else if (c == 's') {
            return new SeaHex(state);
        } else if (c == 'w') {
            return new WoodsHex(roads, rivers, state);
        } else if (c == 'p') {
            return new PlainsHex(roads, rivers, location, state);
        } else if (c == 'b') {
            return new SwampHex(roads, rivers, state);
        } else if (c == 'M') {
            return new MountainHex(roads, rivers, state);
        } else if (c == 'm') {
            return new DesertMountain(roads, rivers, state);
        } else if (c == 'h') {
            return new HillsHex(roads, rivers, state);
        } else if (c == 'f') {
            return new FieldsHex(roads, rivers, state);
        } else if (c == 'd') {
            return new DesertHex(roads, rivers, location, state);
        } else if (c == 'D') {
            return new DesertHills(roads, rivers, state);
        } else if (c == 'o') {
            return new DeepWoodsHex(roads, rivers, state);
        } else if (c == 'j') {
            return new JungleHex(roads, rivers, state);
        } else if (c == 'X') {
            return new WastelandHex(roads, rivers, state);
        } else if (c == 'x') {
            return new WastelandHills(roads, rivers, state);
        }
        throw new IllegalStateException("No hex can be created for token '" + c + "'");
    }

    private static Map<Point, HexContents> makeHexContents() {
        Map<Point, HexContents> contents = new HashMap<>();

        addEvilTower(contents, 34, 1, EXPAND_NORTH);

        addRoadsAndRivers(contents,26, 10, SOUTH_WEST | SOUTH_EAST, 0);
        addRuins(contents, 28, 10, "Grond", SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,30, 10, SOUTH_WEST | SOUTH_EAST, 0);

        addTemple(contents, 15, 11, "Crystal");
        addTown(contents, 21, 11, new EastDurhamTown(), 0, SOUTH_EAST);
        addTown(contents, 25, 11, new CapePaxtonTown(), NORTH, SOUTH_WEST);
        addRoadsAndRivers(contents, 27, 11, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 29, 11, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents,31, 11, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,32, 11, NORTH_WEST | SOUTH, 0);
        addCastle(contents, 36, 11, new ArkvaleCastle(), SOUTH, 0);

        addTown(contents, 32, 12, new UrnTownTown(), NORTH | SOUTH, 0);
        addRoadsAndRivers(contents,36, 12, SOUTH_WEST | NORTH, 0);

        addCastle(contents, 15, 13, new BogdownCastle(), 0, 0);
        addRuins(contents, 19, 13, "Urh", 0, 0);
        addInn(contents, 26, 13, "Waterfront Inn", SOUTH_EAST, 0);
        addRoadsAndRivers(contents,32, 13, NORTH | NORTH_EAST, 0);
        addRoadsAndRivers(contents,33, 13, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,34, 13, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents,35, 13, SOUTH_WEST | NORTH_EAST, 0);

        addRoadsAndRivers(contents,27, 14, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents,28, 14, NORTH_WEST | SOUTH, 0);


        addTown(contents, 14, 15, new RoukonTown(), SOUTH_EAST, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents,28, 15, SOUTH | NORTH, 0);

        addRoadsAndRivers(contents, 15, 16, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 16, 16, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 28, 16, SOUTH | NORTH, SOUTH_WEST | SOUTH | NORTH_WEST);
        addTown(contents, 34, 16, new AshtonshireTown(), 0, 0);

        addRoadsAndRivers(contents, 14, 17, 0, SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 15, 17, 0, SOUTH);
        addRoadsAndRivers(contents, 16, 17, 0, SOUTH_WEST | SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 17, 17, NORTH_WEST | SOUTH_EAST, SOUTH | SOUTH_EAST);
        addTown(contents, 18, 17, new EbonshireTown(), NORTH_WEST, NORTH_WEST);
        addTown(contents, 27, 17, new LowerThelnTown(), 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 28, 17, NORTH_WEST | NORTH | SOUTH, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 29, 17, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 30, 17, 0, SOUTH);
        addTemple(contents, 38, 17, "the Peaks");
        addEvilTower(contents, 49, 17, EXPAND_EAST);

        addRoadsAndRivers(contents, 14, 18, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 15, 18, 0, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 16, 18, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 17, 18, 0, NORTH_WEST | NORTH | SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 18, 18, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 28, 18, NORTH | SOUTH_WEST, NORTH_EAST);
        addRoadsAndRivers(contents, 29, 18, 0, SOUTH_WEST | SOUTH | SOUTH_EAST | NORTH_WEST);
        addRoadsAndRivers(contents, 30, 18, 0, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 31, 18, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 32, 18, 0, SOUTH_WEST);

        addTemple(contents, 14, 19, "the Plains");
        addRoadsAndRivers(contents, 17, 19, 0, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 18, 19, 0, NORTH_WEST | SOUTH_WEST);
        addRoadsAndRivers(contents, 22, 19, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 23, 19, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 24, 19, NORTH_WEST | SOUTH_EAST, 0);
        addInn(contents,26,19, "Crossroads Inn", NORTH_EAST | SOUTH_EAST | SOUTH | SOUTH_WEST, 0);
        addEvilTower(contents, 2, 19, EXPAND_WEST);
        addRoadsAndRivers(contents, 27, 19, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 29, 19, 0, NORTH);
        addRoadsAndRivers(contents, 31, 19, 0, NORTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 32, 19, 0, SOUTH_WEST | NORTH_WEST);
        addRuins(contents, 38, 19, "Ronk", 0, 0);

        addRoadsAndRivers(contents, 17, 20, 0, NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 18, 20, 0, NORTH_WEST | SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 20, 20, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 21, 20, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 25, 20, NORTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 26, 20, NORTH | SOUTH, 0);
        addRoadsAndRivers(contents, 27, 20, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 28, 20, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 31, 20, 0, SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 32, 20, 0, SOUTH_WEST | NORTH_WEST);

        addRoadsAndRivers(contents, 16, 21, 0, SOUTH_EAST);
        addRoadsAndRivers(contents, 17, 21, 0, SOUTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 18, 21, SOUTH_WEST | NORTH_EAST, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 19, 21, SOUTH_WEST | NORTH_EAST, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 20, 21, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 26, 21, NORTH | SOUTH, 0);
        addRoadsAndRivers(contents, 29, 21, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 30, 21, NORTH_WEST | NORTH_EAST, 0);
        addTown(contents, 31, 21, new UpperThelnTown(), SOUTH_WEST, NORTH_EAST);
        addTown(contents, 36, 21, new SaintQuellinTown(), SOUTH_EAST, 0);

        addRoadsAndRivers(contents, 16, 22, 0, NORTH_EAST | SOUTH_EAST);
        addTown(contents, 17, 22, new LittleErindeTown(), NORTH_EAST | SOUTH_EAST, SOUTH_WEST | NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 18, 22, NORTH_WEST | SOUTH, 0);
        addRoadsAndRivers(contents, 20, 22, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 21, 22, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 22, 22, 0, SOUTH_WEST | SOUTH);
        addTown(contents, 23, 22, new AckervilleTown(), 0, SOUTH);
        addRoadsAndRivers(contents, 26, 22, NORTH | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 37, 22, SOUTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 38, 22, SOUTH | NORTH_WEST, 0);

        addRoadsAndRivers(contents, 17, 23, 0, NORTH_WEST);
        addRoadsAndRivers(contents, 18, 23, NORTH | SOUTH_WEST, 0);
        addRoadsAndRivers(contents, 21, 23, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 22, 23, 0, NORTH);
        addRoadsAndRivers(contents, 27, 23, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 28, 23, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 29, 23, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 30, 23, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 38, 23, SOUTH_WEST | NORTH | NORTH_EAST, 0);

        addRuins(contents, 14, 24, "Zand", 0, 0);
        addRoadsAndRivers(contents, 26, 24, 0, SOUTH);
        addRoadsAndRivers(contents, 30, 24, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 31, 24, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 32, 24, NORTH_WEST | SOUTH, 0);
        addRoadsAndRivers(contents, 36, 24, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 37, 24, SOUTH_WEST | NORTH_EAST, 0);

        addRoadsAndRivers(contents, 18, 25, SOUTH_WEST | SOUTH_EAST, 0);
        addInn(contents, 21, 25, "Hunter's Inn", SOUTH, 0);
        addTemple(contents, 24, 25, "the Surf");
        addTown(contents, 26, 25, new SouthMeadhomeTown(), 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 27, 25, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 28, 25, 0, SOUTH_WEST | SOUTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 29, 25, 0, SOUTH | SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 30, 25, 0, NORTH_WEST);
        addTown(contents, 32, 25, new SheffieldTown(), NORTH, 0);
        addCastle(contents, 35, 25, new ArdhCastle(), NORTH_EAST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 36, 25, NORTH_WEST | SOUTH_EAST, 0);
        addTown(contents, 38, 25, new BullsVilleTown(), SOUTH_WEST, 0);

        addCastle(contents, 16, 26, new SunblazeCastle(), NORTH_EAST, 0);
        addRoadsAndRivers(contents, 17, 26, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 19, 26, NORTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 20, 26, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 21, 26, SOUTH_WEST | NORTH, 0);
        addRoadsAndRivers(contents, 27, 26, 0, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 28, 26, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 29, 26, 0, NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 37, 26, NORTH_WEST | NORTH_EAST, 0);
        addEvilTower(contents, 17, 37, EXPAND_SOUTH);

        addEasternContents(contents);

        return contents;
    }

    private static void addEasternContents(Map<Point, HexContents> contents) {
        addRoadsAndRivers(contents, 44, 14, 0, SOUTH);

        addRoadsAndRivers(contents, 44, 15, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 45, 15, 0, SOUTH | SOUTH_WEST);
        addRoadsAndRivers(contents, 46, 15, 0, SOUTH | SOUTH_WEST);

        addRoadsAndRivers(contents, 41, 16, 0, SOUTH_EAST);
        addRoadsAndRivers(contents, 42, 16, 0, SOUTH_WEST | NORTH_WEST);
        addRoadsAndRivers(contents, 45, 16, 0, NORTH_EAST | NORTH);
        addRoadsAndRivers(contents, 46, 16, 0, SOUTH_EAST | NORTH_EAST | NORTH);
        addRoadsAndRivers(contents, 47, 16, 0, SOUTH_WEST);

        addRoadsAndRivers(contents, 41, 17, 0, SOUTH_EAST | NORTH_EAST);
        addRoadsAndRivers(contents, 42, 17, 0, SOUTH | SOUTH_WEST | SOUTH_EAST | NORTH_WEST);
        addRoadsAndRivers(contents, 44, 17, 0, SOUTH | SOUTH_WEST);
        addRoadsAndRivers(contents, 46, 17, 0, NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 47, 17, 0, SOUTH_WEST | NORTH_WEST);
        addRoadsAndRivers(contents, 50, 17, SOUTH | NORTH_WEST, 0);

        addRoadsAndRivers(contents, 41, 18, 0, NORTH_EAST);
        addRoadsAndRivers(contents, 42, 18, 0, NORTH);
        addRoadsAndRivers(contents, 43, 18, 0, NORTH | NORTH_EAST | NORTH_WEST);
        addRoadsAndRivers(contents, 44, 18, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 45, 18, 0, SOUTH | SOUTH_WEST);
        addRoadsAndRivers(contents, 46, 18, 0, SOUTH_WEST | NORTH_EAST);
        addRoadsAndRivers(contents, 47, 18, 0, SOUTH_WEST | SOUTH | NORTH_WEST);
        addRoadsAndRivers(contents, 48, 18, 0, SOUTH_WEST);
        addRoadsAndRivers(contents, 50, 18, SOUTH_EAST | NORTH, 0);

        addRoadsAndRivers(contents, 45, 19, 0, NORTH_EAST | SOUTH_EAST | NORTH);
        addRoadsAndRivers(contents, 46, 19, 0, SOUTH_EAST | SOUTH | SOUTH_WEST | NORTH_WEST);
        addRoadsAndRivers(contents, 47, 19, 0, SOUTH | SOUTH_EAST | NORTH_EAST | NORTH);
        addRoadsAndRivers(contents, 48, 19, 0, NORTH_WEST);
        addRoadsAndRivers(contents, 51, 19, SOUTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 52, 19, SOUTH | NORTH_WEST, 0);

        addRoadsAndRivers(contents, 45, 20, 0, NORTH_EAST);
        addRoadsAndRivers(contents, 46, 20, 0, NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 47, 20, 0, SOUTH_WEST | NORTH_WEST | NORTH);
        addRoadsAndRivers(contents, 48, 20, 0, SOUTH);
        addRoadsAndRivers(contents, 52, 20, SOUTH_WEST | NORTH,0);

        addRoadsAndRivers(contents, 48, 21, 0, NORTH_WEST | NORTH | NORTH_EAST);
        addRoadsAndRivers(contents, 49, 21, 0, SOUTH | SOUTH_WEST);
        addRoadsAndRivers(contents, 50, 21, SOUTH_WEST | NORTH_EAST,SOUTH_WEST);
        addInn(contents, 51, 21, "Inn of the Vulture", SOUTH_WEST | NORTH_EAST, 0);

        addRoadsAndRivers(contents, 47, 22, SOUTH_EAST | SOUTH_WEST, 0);
        addRoadsAndRivers(contents, 48, 22, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 49, 22, SOUTH_WEST | NORTH_EAST, NORTH_EAST | NORTH | SOUTH_EAST);
        addRoadsAndRivers(contents, 50, 22, 0, NORTH_WEST | SOUTH_WEST);

        addRoadsAndRivers(contents, 39, 23, SOUTH_EAST | SOUTH_WEST, 0);
        addRoadsAndRivers(contents, 40, 23, SOUTH_EAST | NORTH_WEST, 0);
        addRoadsAndRivers(contents, 42, 23, SOUTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 43, 23, SOUTH_WEST | SOUTH_EAST, 0);
        addRoadsAndRivers(contents, 44, 23, NORTH_EAST, 0);
        addRoadsAndRivers(contents, 45, 23, NORTH_EAST, 0);
        addRoadsAndRivers(contents, 49, 23, 0, NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 50, 23, 0, NORTH_WEST | SOUTH_WEST | SOUTH);

        addRoadsAndRivers(contents, 41, 24, NORTH_WEST | NORTH_EAST, 0);
        addRoadsAndRivers(contents, 49, 24, 0, NORTH_EAST);
        addRoadsAndRivers(contents, 50, 24, 0, NORTH_EAST | NORTH);
        addRoadsAndRivers(contents, 51, 24, 0, SOUTH_WEST | SOUTH);
        addRoadsAndRivers(contents, 52, 24, 0, SOUTH_WEST);

        addRoadsAndRivers(contents, 51, 25, 0, NORTH | NORTH_EAST | SOUTH_EAST);
        addRoadsAndRivers(contents, 52, 25, 0, NORTH_WEST);
    }

    private static void addRoadsAndRivers(Map<Point, HexContents> contents, int x, int y, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(null, roads, rivers));
    }

    private static void addRuins(Map<Point, HexContents> contents, int x, int y, String ruinsName, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new RuinsLocation(ruinsName), roads, rivers));
    }

    private static void addEvilTower(Map<Point, HexContents> contents, int x, int y, int expandDirection) {
        contents.put(new Point(x, y), new HexContents(new AncientStrongholdLocation(expandDirection), 0, 0));
    }

    private static void addTemple(Map<Point, HexContents> contents, int x, int y, String templeName) {
        contents.put(new Point(x, y), new HexContents(new TempleLocation(templeName), 0, 0));
    }

    private static void addCastle(Map<Point, HexContents> contents, int x, int y, CastleLocation castle, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(castle, roads, rivers));
    }

    private static void addInn(Map<Point, HexContents> contents, int x, int y, String innName, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(new InnLocation(innName), roads, rivers));
    }

    private static void addTown(Map<Point, HexContents> contents, int x, int y, TownLocation town, int roads, int rivers) {
        contents.put(new Point(x, y), new HexContents(town, roads, rivers));
    }


    public static WorldHex[][] buildWorld() {
        WorldHex[][] hexes = new WorldHex[WORLD_WIDTH][WORLD_HEIGHT];
        Map<Point, HexContents> hexContents = makeHexContents();
        for (int y = 0; y < worldTemplate.length; ++y) {
            for (int x = 0; x < WORLD_WIDTH; ++x) {
                int state = getStateForXY(x, y);
                HexContents contents = hexContents.get(new Point(x, y));
                hexes[x][y] = makeHex(worldTemplate[y].charAt(x), contents, state);
                if (contents != null && contents.location != null) {
                    contents.location.setHex(hexes[x][y]);
                }
            }
        }

        makeSeaBorders(hexes);

        return hexes;
    }

    public static int getStateForXY(int x, int y) {
        int state = ORIGINAL;
        if (x < EXTRA_WIDTH) {
            state |= EXPAND_WEST;
        } else if (x >= WORLD_WIDTH - EXTRA_WIDTH) {
            state |= EXPAND_EAST;
        }
        if (y < EXTRA_HEIGHT) {
            state |= EXPAND_NORTH;
        } else if (y >= WORLD_HEIGHT - EXTRA_HEIGHT) {
            state |= EXPAND_SOUTH;
        }
        return state;
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

    public static Rectangle getWorldBounds(int currentState) {
        Rectangle bounds = new Rectangle(EXTRA_WIDTH, EXTRA_HEIGHT, WORLD_WIDTH - 2*EXTRA_WIDTH, WORLD_HEIGHT - 2*EXTRA_HEIGHT);
        if ((currentState & EXPAND_WEST) > 0) {
            bounds.x -= EXTRA_WIDTH;
            bounds.width += EXTRA_WIDTH;
        }
        if ((currentState & EXPAND_EAST) > 0) {
            bounds.width += EXTRA_WIDTH;
        }
        if ((currentState & EXPAND_NORTH) > 0) {
            bounds.y -= EXTRA_HEIGHT;
            bounds.height += EXTRA_HEIGHT;
        }
        if ((currentState & EXPAND_SOUTH) > 0) {
            bounds.height += EXTRA_HEIGHT;
        }
        return bounds;
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
