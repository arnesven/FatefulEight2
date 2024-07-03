package model.map.wars;

import model.states.battle.BattleTerrain;
import model.states.battle.HillsBattleTerrain;
import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SparselyHillyBattleSite extends PitchedBattleSite {

    public SparselyHillyBattleSite(Point point, MyColors color, String name) {
        super(point, color, name);
    }

    @Override
    public List<MyPair<Point, BattleTerrain>> getTerrain() {
        List<Integer> xs = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        List<Integer> ys = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8));
        Collections.shuffle(xs);
        Collections.shuffle(ys);
        List<MyPair<Point, BattleTerrain>> result = new ArrayList<>();
        for (int i = MyRandom.randInt(3, 7); i >= 0; --i) {
            result.add(new MyPair<>(new Point(xs.remove(0), ys.remove(0)), new HillsBattleTerrain(getGroundColor())));
        }
        return result;
    }
}
