package model.ruins;

import model.Model;
import model.ruins.objects.FatueKeyObject;
import model.ruins.objects.MinesOfMiseryMonsterFactory;
import model.ruins.objects.MonsterFactory;
import model.ruins.objects.WestWingMonsterFactory;
import model.ruins.themes.*;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonMaker {

    private static final List<DungeonTheme> ALL_BRICK_THEMES =
            List.of(new GrayBrickTheme(), new PurpleBrickTheme(), new RedBrickTheme(),
                    new BlueBrickTheme(), new GreenBrickTheme());
    private static final List<DungeonTheme> ALL_CAVE_THEMES =
            List.of(new GrayCaveTheme(), new PurpleCaveTheme(), new RedCaveTheme(),
                    new BlueCaveTheme(), new GreenCaveTheme());
    private static final List<DungeonTheme> ALL_RUINS_THEMES =
            List.of(new GrayRuinsTheme(), new PurpleRuinsTheme(), new RedRuinsTheme(),
                    new BlueRuinsTheme(), new GreenRuinsTheme());
    public static final int NUMBER_OF_UPPER_LEVELS = 2;

    public static List<DungeonLevel> makeRandomDungeon(int roomsTarget, int levelMinSize, int levelMaxSize, boolean isRuins) {
        System.out.println("Creating a random dungeon...");
        Random random = new Random();
        List<DungeonLevel> levels = new ArrayList<>();
        int i = 0;
        for (; roomsTarget >= levelMinSize*levelMinSize ; ++i) {
            int levelSize;
            do {
                levelSize = MyRandom.randInt(levelMinSize, levelMaxSize);
            } while (roomsTarget < levelSize*levelSize);
            roomsTarget -= levelSize*levelSize;
            levels.add(new DungeonLevel(random, i == 0, levelSize, makeDungeonTheme(i, isRuins), new MonsterFactory()));
            System.out.println(" Level " + i + " is " + levelSize + "x" + levelSize);
        }
        System.out.println(" Level " + i + " is the final level");
        levels.add(new FinalDungeonLevel(random, makeDungeonTheme(i, isRuins)));
        return levels;
    }

    private static DungeonTheme makeDungeonTheme(int i, boolean isRuins) {
        if (isRuins) {
            if (i < NUMBER_OF_UPPER_LEVELS) {
                return MyRandom.sample(ALL_RUINS_THEMES);
            }
            return MyRandom.sample(ALL_CAVE_THEMES);
        }
        if (i < NUMBER_OF_UPPER_LEVELS) {
            return MyRandom.sample(ALL_BRICK_THEMES);
        }
        return MyRandom.sample(ALL_RUINS_THEMES);
    }

    public static List<DungeonLevel> makeGardenDungeon(int size) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new DungeonLevel(random, true, size, new GardenDungeonLevelConfig()));
        levels.add(new FinalDungeonLevel(random, new RedRuinsTheme()));
        return levels;
    }

    public static List<DungeonLevel> makeTallSpireDungeon() {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonLevelConfig config = new TallSpireDungeonConfig();
        levels.add(new FinalDungeonLevel(random, config.getTheme()));
        int maxLevels = MyRandom.randInt(8, 13);
        for (int i = 0; i < maxLevels; i++) {
            levels.add(new DungeonLevel(random, false, 3, config));
        }
        levels.add(new FinalDungeonLevel(random, config.getTheme()));
        return levels;
    }

    public static List<DungeonLevel> makeFortressAtUtmostEdge() {
        return makeRandomDungeon(MyRandom.randInt(450, 500), 5, 10, false);
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
        levels.add(new FinalDungeonLevel(random, theme));
        return levels;
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
        levels.add(new FinalDungeonLevel(random, theme));
        return levels;
    }
}
