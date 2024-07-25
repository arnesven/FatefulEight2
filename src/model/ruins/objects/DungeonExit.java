package model.ruins.objects;

import model.Model;
import model.states.ExploreRuinsState;

import java.awt.*;

public class DungeonExit extends StairsUp {
    public DungeonExit(Point p) {
        super(p);
    }

    public DungeonExit() {
        this(new Point(1, 0));
    }

    @Override
    public String getDescription() {
        return "Exit dungeon";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.print("Are you sure you want to exit the dungeon? (Y/N) ");
        if (state.yesNoInput()) {
            state.setDungeonExited(true);
        }
    }
}
