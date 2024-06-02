package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.objects.FatueKeyObject;
import model.ruins.objects.MinesOfMiseryMonsterFactory;
import model.ruins.objects.MonsterFactory;
import model.ruins.themes.DungeonTheme;
import model.ruins.themes.GrayCaveTheme;
import model.states.dailyaction.AdvancedDailyActionState;
import view.MyColors;
import view.subviews.CaveTheme;
import view.subviews.CombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MinesOfMiseryNode extends FatueDungeonNode {
    public MinesOfMiseryNode() {
        super("Mines of Misery", true,"These narrow and treacherous steps " +
                "lead down to the dank mines below the fortress. Would you like to explore them?");
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        return new RuinsDungeon(makeMinesOfMiseryDungeon(model));
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new CaveTheme();
    }

    public static List<DungeonLevel> makeMinesOfMiseryDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonTheme theme = new GrayCaveTheme();
        MonsterFactory monsterFactory = new MinesOfMiseryMonsterFactory(model);
        KeySpawningDungeonLevelConfig keySpawningConfig =
                new KeySpawningDungeonLevelConfig(theme, monsterFactory, new FatueKeyObject(MyColors.DARK_RED));
        DungeonLevel level = null;
        do {
            level = new DungeonLevel(random, true, 12, keySpawningConfig);
            System.err.println("Key did not spawn in mines of misery, trying again.");
        } while (!keySpawningConfig.isKeySpawned());
        levels.add(level);
        FinalDungeonLevel finalLevel = new FinalDungeonLevel(random, theme);
        finalLevel.setFinalRoom(new FatueStaffRoom());
        levels.add(finalLevel);
        return levels;
    }
}
