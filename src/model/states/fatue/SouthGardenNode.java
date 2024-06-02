package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.objects.FatueKeyObject;
import model.ruins.themes.GreenRuinsTheme;
import view.MyColors;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SouthGardenNode extends FatueDungeonNode {
    public SouthGardenNode() {
        super("South Garden", true,"This passage leads out into a " +
                "smallish garden on the castle exterior. Would you like to explore it?");
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        return new RuinsDungeon(makeGardenDungeon(9));
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new GrassCombatTheme(); // TODO: Make garden combat theme
    }

    public static List<DungeonLevel> makeGardenDungeon(int size) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new DungeonLevel(random, false, size, new GardenDungeonLevelConfig()));
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(random, new GreenRuinsTheme());
        finalLevel.setFinalRoom(new JadeKeyRoom());
        levels.add(finalLevel);
        return levels;
    }

    private static class JadeKeyRoom extends DungeonRoom {
        public JadeKeyRoom() {
            addObject(new FatueKeyObject(MyColors.DARK_GREEN, true));
        }
    }
}
