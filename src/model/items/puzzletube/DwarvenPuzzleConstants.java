package model.items.puzzletube;

import model.map.WorldBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public interface DwarvenPuzzleConstants {

    List<Point> MYSTERY_POSITIONS = List.of(
            new Point(24, 20),  // Bogdown
            new Point(26, 18),  // Ardh
            new Point(29, 18),  // Arkvale
            new Point(23, 24)); // Sunblaze

    Point WOODS_NEAR_EBONSHIRE    = new Point(19, 19);
    Point SW_DESERT_HILLS         = new Point(14, 26);
    Point SW_DESERT_MOUNTAIN      = new Point(20, 23);
    Point SE_MOUNTAIN             = new Point(33, 27);
    Point E_DESERT_HILLS          = new Point(34, 21);
    Point FIELDS_NEAR_ASHTONSHIRE = new Point(33, 18);
    Point PLAINS_NEAR_URNTOWN     = new Point(31, 14);
    Point N_TUNDRA_HILLS          = new Point(32, 10);
    Point NW_TUNDRA               = new Point(18, 10);
    Point NW_SWAMP                = new Point(14, 12);

    Map<Point, Point> LOCATION_MAP = makeLocationMap();
    String TOYMAKER_FIRST_NAME = "Ralki";
    String TOYMAKER_LAST_NAME = "Blackfoot";
    String TOYMAKER_NAME = TOYMAKER_FIRST_NAME + " " + TOYMAKER_LAST_NAME;

    static Map<Point, Point> makeLocationMap() {
        Map<Point, Point> result = new HashMap<>();
        result.put(WorldBuilder.TEMPLE_CRYSTAL,   NW_SWAMP);
        result.put(NW_SWAMP,                      WorldBuilder.TOWN_ROUKON);
        result.put(WorldBuilder.TOWN_ROUKON,      WOODS_NEAR_EBONSHIRE);
        result.put(WOODS_NEAR_EBONSHIRE,          WorldBuilder.TEMPLE_PLAINS);
        result.put(WorldBuilder.TEMPLE_PLAINS,    SW_DESERT_HILLS);
        result.put(SW_DESERT_HILLS,               SW_DESERT_MOUNTAIN);
        result.put(SW_DESERT_MOUNTAIN,            WorldBuilder.TEMPLE_SURF);
        result.put(WorldBuilder.TEMPLE_SURF,      WorldBuilder.TOWN_SHEFFIELD);
        result.put(WorldBuilder.TOWN_SHEFFIELD,   SE_MOUNTAIN);
        result.put(SE_MOUNTAIN,                   WorldBuilder.TOWN_SAINT_QUELLIN);
        result.put(WorldBuilder.TOWN_SAINT_QUELLIN, E_DESERT_HILLS);
        result.put(E_DESERT_HILLS,                WorldBuilder.TOWN_UPPER_THELN);
        result.put(WorldBuilder.TOWN_UPPER_THELN, WorldBuilder.TOWN_LOWER_THELN);
        result.put(WorldBuilder.TOWN_LOWER_THELN, FIELDS_NEAR_ASHTONSHIRE);
        result.put(FIELDS_NEAR_ASHTONSHIRE,       WorldBuilder.TEMPLE_PEAKS);
        result.put(WorldBuilder.TEMPLE_PEAKS,     PLAINS_NEAR_URNTOWN);
        result.put(PLAINS_NEAR_URNTOWN,           N_TUNDRA_HILLS);
        result.put(N_TUNDRA_HILLS,                WorldBuilder.TOWN_CAPE_PAXTON);
        result.put(WorldBuilder.TOWN_CAPE_PAXTON, NW_TUNDRA);
        result.put(NW_TUNDRA,                     WorldBuilder.TEMPLE_CRYSTAL);
        return result;
    }
}
