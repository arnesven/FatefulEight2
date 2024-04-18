package model.ruins;

import model.ruins.objects.MonsterFactory;
import model.ruins.objects.StairsUp;
import model.ruins.themes.DungeonTheme;

import java.awt.*;
import java.util.Random;

public class FinalDungeonLevel extends DungeonLevel {
    public FinalDungeonLevel(Random random, DungeonTheme theme) {
        super(random, false, 1, theme, new MonsterFactory());
        setFinalRoom(new BossRoom());
    }

    public void setFinalRoom(DungeonRoom room) {
        setRoom(0, 0, room);
        setStartingPoint(new Point(0, 0));
        getRoom(getStartingPoint()).setConnection(0, new StairsUp(new Point(1, 0)));
    }

    @Override
    protected boolean buildRandomLevel(boolean firstLevel) { return true; }
}
