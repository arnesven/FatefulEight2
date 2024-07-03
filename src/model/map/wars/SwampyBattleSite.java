package model.map.wars;

import model.states.battle.BattleTerrain;
import util.MyPair;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class SwampyBattleSite extends PitchedBattleSite {
    private static final String TERRAIN_STRING =
                    ":..:...:" +
                    ".:::@@.." +
                    "@~:@.@@." +
                    "..@@.:::" +
                    ".@@@~@@." +
                    "@@~.:@@~" +
                    ":.@:.::." +
                    "..@@@..." +
                    "::.:...:";

    public SwampyBattleSite(Point point, MyColors color, String name) {
        super(point, color, name);
    }

    @Override
    public List<MyPair<Point, BattleTerrain>> getTerrain() {
        return makeTerrain(TERRAIN_STRING, getGroundColor());
    }
}
