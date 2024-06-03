package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.objects.DousableFire;
import model.ruins.objects.FatueGardenMonsterFactory;
import model.ruins.objects.FatueSunDialObject;
import model.ruins.themes.GardenDungeonTheme;
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
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(random, new GardenDungeonTheme());
        finalLevel.setFinalRoom(new JadeKeyRoom());
        levels.add(finalLevel);
        return levels;
    }

    private static class JadeKeyRoom extends DungeonRoom {
        private List<DousableFire> fires = new ArrayList<>();
        public JadeKeyRoom() {
            super(5, 5);
            FatueSunDialObject sundial = new FatueSunDialObject(MyColors.DARK_GREEN);
            addObject(sundial);
            DousableFire d1 = new DousableFire(5, 5, sundial);
            addObject(d1);
            fires.add(d1);

            DousableFire d2 = new DousableFire(5, 1, sundial);
            fires.add(d2);
            addObject(d2);

            DousableFire d3 = new DousableFire(1, 5, sundial);
            fires.add(d3);
            addObject(d3);

            d1.setAllFires(fires);
            d2.setAllFires(fires);
            d3.setAllFires(fires);
        }

    }

}
