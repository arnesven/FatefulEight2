package model.map.wars;

import model.states.battle.*;
import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PitchedBattleSite implements Serializable {
    private final Point position;
    private final MyColors groundColor;
    private final String name;

    public PitchedBattleSite(Point pos, MyColors color, String name) {
        this.position = pos;
        this.groundColor = color;
        this.name = name;
    }

    public List<MyPair<Point, BattleTerrain>> getTerrain() {
        List<MyPair<Integer, Integer>> positions = new ArrayList<>();
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 9; ++y) {
                positions.add(new MyPair<>(x, y));
            }
        }
        Collections.shuffle(positions);

        List<MyPair<Point, BattleTerrain>> result = new ArrayList<>();
        for (int i = MyRandom.randInt(3, 9); i > 0; --i) {
            MyPair<Integer, Integer> pos = positions.removeFirst();
            result.add(new MyPair<>(new Point(pos.first, pos.second), new WoodsBattleTerrain()));
        }
        for (int i = MyRandom.randInt(2, 6); i > 0; --i) {
            MyPair<Integer, Integer> pos = positions.removeFirst();
            result.add(new MyPair<>(new Point(pos.first, pos.second), new HillsBattleTerrain(getGroundColor())));
        }
        return result;
    }

    public Point getPosition() {
        return position;
    }

    public MyColors getGroundColor() {
        return groundColor;
    }

    public String getName() {
        return name;
    }

    protected static List<MyPair<Point, BattleTerrain>> makeTerrain(String data, MyColors hillColor) {
        if (data.length() != BattleState.BATTLE_GRID_HEIGHT * BattleState.BATTLE_GRID_WIDTH) {
            throw new IllegalArgumentException("Bad data string for makeTerrain: " + data);
        }
        List<MyPair<Point, BattleTerrain>> list = new ArrayList<>();
        for (int y = 0; y < BattleState.BATTLE_GRID_HEIGHT; ++y) {
            for (int x = 0; x < BattleState.BATTLE_GRID_WIDTH; ++x) {
                int index = y * BattleState.BATTLE_GRID_WIDTH + x;
                char c = data.charAt(index);
                BattleTerrain terr = makeTerrainFromChar(c, hillColor);
                if (terr != null) {
                    list.add(new MyPair<>(new Point(x, y), terr));
                }
            }
        }
        return list;
    }

    private static BattleTerrain makeTerrainFromChar(char c, MyColors hillColor) {
        switch (c) {
            case ':' :
                return new WoodsBattleTerrain();
            case '#' :
                return new DenseWoodsBattleTerrain();
            case 'A' :
                return new HillsBattleTerrain(hillColor);
            case '~' :
                return new WaterBattleTerrain();
            case '@' :
                return new SwampBattleTerrain();
        }
        return null;
    }
}
