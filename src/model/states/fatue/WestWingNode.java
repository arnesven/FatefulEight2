package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.configs.KeySpawningDungeonLevelConfig;
import model.ruins.objects.FatueKeyObject;
import model.ruins.factories.MonsterFactory;
import model.ruins.factories.WestWingMonsterFactory;
import model.ruins.themes.DungeonTheme;
import model.ruins.themes.RuinsTheme;
import view.MyColors;
import view.combat.CombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class WestWingNode extends FatueDungeonNode {
    public WestWingNode() {
        super("West Wing", true,"This passage leads to a dilapidated wing " +
                "of the fortress. Would you like to explore it?");
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        return new RuinsDungeon(makeWestWingDungeon(model));
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new view.combat.DungeonTheme();
    }

    public static List<DungeonLevel> makeWestWingDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonTheme theme = new RuinsTheme(MyColors.GOLD, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BLACK);
        MonsterFactory monsterFactory = new WestWingMonsterFactory(model);
        levels.add(new DungeonLevel(random, true, 8, theme, monsterFactory));
        KeySpawningDungeonLevelConfig keySpawningConfig =
                new KeySpawningDungeonLevelConfig(theme, monsterFactory, new FatueKeyObject(MyColors.GOLD));
        DungeonLevel level2 = null;
        do {
            level2 = new DungeonLevel(random, false, 8, keySpawningConfig);
            System.err.println("Key did not spawn in west wing level 2, trying again.");
        } while (!keySpawningConfig.isKeySpawned());
        levels.add(level2);
        FinalDungeonLevel finalDungeonLevel = new FinalDungeonLevel(random, theme);
        finalDungeonLevel.setFinalRoom(new FatueStaffRoom());
        levels.add(finalDungeonLevel);
        return levels;
    }
}
