package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.configs.GardenDungeonLevelConfig;
import model.ruins.objects.DousableFire;
import model.ruins.factories.FatueGardenMonsterFactory;
import model.ruins.objects.FatueSunDialObject;
import model.ruins.themes.GardenDungeonTheme;
import view.MyColors;
import view.combat.GardenCombatTheme;
import view.combat.CombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SouthGardenNode extends FatueDungeonNode {
    private final MyColors givesKeyColor;

    public SouthGardenNode(MyColors givesKeyColor) {
        super("South Garden", true,"This passage leads out into a " +
                "smallish garden on the castle exterior. Would you like to explore it?");
        this.givesKeyColor = givesKeyColor;
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new DungeonLevel(random, false, 9,
                new GardenDungeonLevelConfig(new FatueGardenMonsterFactory(model))));
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(random, new GardenDungeonTheme());
        finalLevel.setFinalRoom(new SundialRoom(givesKeyColor));
        levels.add(finalLevel);
        return new RuinsDungeon(levels);
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new GardenCombatTheme();
    }

    private static class SundialRoom extends DungeonRoom {
        private List<DousableFire> fires = new ArrayList<>();
        public SundialRoom(MyColors givesKeyColor) {
            super(5, 5);
            FatueSunDialObject sundial = new FatueSunDialObject(givesKeyColor);
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
