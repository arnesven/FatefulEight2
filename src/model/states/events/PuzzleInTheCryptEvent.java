package model.states.events;

import model.Model;
import model.items.Item;
import model.ruins.*;
import model.ruins.objects.DungeonObject;
import model.ruins.objects.HiddenChestObject;
import model.ruins.themes.GrayRuinsTheme;
import model.ruins.themes.RedRuinsTheme;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleInTheCryptEvent extends DailyEventState {
    public PuzzleInTheCryptEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You descend into a large chamber with many pressure plates here.");
        leaderSay("Some kind of puzzle?");
        Random random = new Random();
        List<DungeonLevel> levels = new ArrayList<>();
        levels.add(new PuzzleDungeonLevel(model, random, true, true, new GrayRuinsTheme()));

        DungeonRoom finalRoom = new LargeDungeonRoom();
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(model, random, new GrayRuinsTheme(), finalRoom);
        finalRoom.addObject(new FinalHiddenChest(model));
        levels.add(finalLevel);

        RuinsDungeon dungeon = new RuinsDungeon(levels);
        ExploreRuinsState explore = new ExploreRuinsState(model, dungeon, "Crypt Puzzle");
        explore.run(model);
        println("You exit the crypt.");
    }

    private static class FinalHiddenChest extends HiddenChestObject {
        public FinalHiddenChest(Model model) {
            super(model.getItemDeck().draw(1, 0.99).get(0));
            setInternalPosition(new Point(3, 3));
        }

        @Override
        public void doAction(Model model, ExploreRuinsState state) {
            super.doAction(model, state);
            state.print("You have completed this dungeon. Press enter to continue.");
            state.waitForReturn();
            state.getDungeon().setCompleted(true);
            state.setDungeonExited(true);
        }
    }
}
