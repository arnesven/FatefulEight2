package model.ruins;

import model.Model;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.StairsDown;
import model.ruins.objects.StairsUp;
import model.ruins.themes.DungeonTheme;

import java.awt.*;
import java.util.Random;

public class FinalDungeonLevel extends DungeonLevel {
    public FinalDungeonLevel(Model model, Random random, DungeonTheme theme, DungeonRoom room) {
        super(model, random, false, 1, theme, new MonsterFactory());
        setFinalRoom(room);
    }

    public FinalDungeonLevel(Model model, Random random, DungeonTheme theme) {
        this(model, random, theme, new BossRoom());
    }

    public void setFinalRoom(DungeonRoom room, boolean below) {
        setRoom(0, 0, room);
        setStartingPoint(new Point(0, 0));
        setDescentPoint(new Point(0, 0));
        if (below) {
            getRoom(getStartingPoint()).setConnection(0, new StairsUp(new Point(1, 0)));
        } else {
            getRoom(getStartingPoint()).setConnection(0, new StairsDown(new Point(1, 0)));
        }
    }

    public void setFinalRoom(DungeonRoom room) {
        setFinalRoom(room, true);
    }

    @Override
    protected boolean buildRandomLevel(Model model, boolean firstLevel) { return true; }

    @Override
    public boolean showExitIcon() {
        return false;
    }

    @Override
    public boolean showMapIcon() {
        return false;
    }
}
