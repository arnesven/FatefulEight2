package model.states.fatue;

import model.Model;
import model.items.special.FashionableSash;
import model.ruins.*;
import model.ruins.configs.KeySpawningDungeonLevelConfig;
import model.ruins.objects.FatueKeyObject;
import model.ruins.factories.MonsterFactory;
import model.ruins.factories.WestWingMonsterFactory;
import model.ruins.objects.HiddenChestObject;
import model.ruins.themes.DungeonTheme;
import model.ruins.themes.RuinsTheme;
import view.MyColors;
import view.combat.CombatTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class WestWingNode extends FatueDungeonNode {
    private final MyColors givesKeyColor;

    public WestWingNode(MyColors givesKeyColor) {
        super("West Wing", true,"This passage leads to a dilapidated wing " +
                "of the fortress. Would you like to explore it?");
        this.givesKeyColor = givesKeyColor;
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new view.combat.DungeonTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonTheme theme = new RuinsTheme(MyColors.GOLD, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.BLACK);
        MonsterFactory monsterFactory = new WestWingMonsterFactory();
        levels.add(new DungeonLevel(model, random, true, 8, theme, monsterFactory));
        DungeonLevel level2 = null;
        do {
            KeySpawningDungeonLevelConfig keySpawningConfig =
                    new KeySpawningDungeonLevelConfig(theme, monsterFactory, givesKeyColor, true);
            level2 = new DungeonLevel(model, random, false, 8, keySpawningConfig);
            System.err.println("Key did not spawn in west wing level 2, trying again.");
            if (keySpawningConfig.isKeySpawned()) {
                break;
            }
        } while (true);
        levels.add(level2);
        FinalDungeonLevel finalDungeonLevel = new FinalDungeonLevel(model, random, theme);
        finalDungeonLevel.setFinalRoom(new FatueStaffRoom());
        levels.add(finalDungeonLevel);
        return new RuinsDungeon(levels);
    }
}
