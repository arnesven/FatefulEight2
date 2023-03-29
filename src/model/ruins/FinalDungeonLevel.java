package model.ruins;

import java.awt.*;
import java.util.Random;

public class FinalDungeonLevel extends DungeonLevel {
    public FinalDungeonLevel(Random random) {
        super(random, false, 1);
        setRoom(0, 0, new BossRoom());
        setStartingPoint(new Point(0, 0));
        getRoom(getStartingPoint()).setConnection(0, new StairsUp(new Point(1, 0)));
    }

    @Override
    protected void buildRandomLevel(boolean firstLevel) { }
}
