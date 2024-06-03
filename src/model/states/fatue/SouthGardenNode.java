package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.objects.FatueGardenMonsterFactory;
import model.ruins.objects.FatueKeyObject;
import model.ruins.themes.GreenRuinsTheme;
import view.MyColors;
import view.combat.GardenCombatTheme;
import view.combat.CombatTheme;

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
        return new RuinsDungeon(makeGardenDungeon(model, 9));
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new GardenCombatTheme();
    }

    public static List<DungeonLevel> makeGardenDungeon(Model model, int size) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new DungeonLevel(random, false, size, new GardenDungeonLevelConfig(new FatueGardenMonsterFactory(model))));
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
