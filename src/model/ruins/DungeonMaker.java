package model.ruins;

import model.Model;
import model.items.Prevalence;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.GardenDungeonLevelConfig;
import model.ruins.configs.TallSpireDungeonConfig;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.HiddenChestObject;
import model.ruins.themes.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonMaker {

    private static final List<DungeonTheme> ALL_BRICK_THEMES =
            List.of(new PurpleAndRedBrickTheme(), new BlueAndWhiteBrickTheme(),
                    new GreenAndYellowBrickTheme(),
                    new GrayBrickTheme(), new PurpleBrickTheme(), new RedBrickTheme(),
                    new BlueBrickTheme(), new GreenBrickTheme());
    private static final List<DungeonTheme> ALL_CAVE_THEMES =
            List.of(new BeigeAndGrayCaveTheme(), new BlueAndGreenCaveTheme(),
                    new RedAndOrangeCaveTheme(), new GrayCaveTheme(),
                    new PurpleCaveTheme(), new RedCaveTheme(),
                    new BlueCaveTheme(), new GreenCaveTheme());
    private static final List<DungeonTheme> ALL_RUINS_THEMES =
            List.of(new BlueAndGrayRuinsTheme(),
            new GreenAndRedDungeonTheme(), new PurpleAndBlackTheme(),
            new GrayRuinsTheme(), new PurpleRuinsTheme(), new RedRuinsTheme(),
            new BlueRuinsTheme(), new GreenRuinsTheme());
    public static final int NUMBER_OF_UPPER_LEVELS = 2;

    public static List<DungeonLevel> makeRandomDungeon(Model model, int roomsTarget, int levelMinSize, int levelMaxSize, boolean isRuins) {
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
            DungeonLevelConfig config = new DungeonLevelConfig(makeDungeonTheme(i, isRuins), new MonsterFactory());
            config.addRequiredDeadEndObject(new HiddenChestObject(model.getItemDeck().draw(1, Prevalence.rare).get(0)), 0.05);
            if (config.allRequiredObjectsPlaced()) {
                System.out.println("Hidden chest on level " + i + " got placed.");
            }
            levels.add(new DungeonLevel(model, random, i == 0, levelSize, config));
            System.out.println(" Level " + i + " is " + levelSize + "x" + levelSize);
        }
        System.out.println(" Level " + i + " is the final level");
        levels.add(new FinalDungeonLevel(model, random, makeDungeonTheme(i, isRuins)));
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

    public static List<DungeonLevel> makeGardenDungeon(Model model, int size) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new DungeonLevel(model, random, true, size, new GardenDungeonLevelConfig()));
        levels.add(new FinalDungeonLevel(model, random, new RedRuinsTheme()));
        return levels;
    }

    public static List<DungeonLevel> makeTallSpireDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonLevelConfig config = new TallSpireDungeonConfig();
        levels.add(new FinalDungeonLevel(model, random, config.getTheme()));
        int maxLevels = MyRandom.randInt(8, 13);
        for (int i = 0; i < maxLevels; i++) {
            levels.add(new DungeonLevel(model, random, false, 3, config));
        }
        levels.add(new FinalDungeonLevel(model, random, config.getTheme()));
        return levels;
    }
}
