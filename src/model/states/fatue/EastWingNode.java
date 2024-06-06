package model.states.fatue;

import model.Model;
import model.ruins.*;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.NoLeversDungeonConfig;
import model.ruins.configs.PitfallDungeonConfig;
import model.ruins.factories.UndeadMonsterFactory;
import model.ruins.objects.*;
import model.ruins.themes.PurpleBrickTheme;
import model.ruins.themes.PurpleRuinsTheme;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EastWingNode extends KeyRequiredFatueDungeonNode {
    private final MyColors givesKeyColor;

    public EastWingNode(MyColors requiresKeyColor, MyColors givesKeyColor) {
        super("East Wing", false, requiresKeyColor,
                "This hallway leads to the rundown eastern wing. " +
                        "It's dark and quiet this way, do you want to continue?");
        this.givesKeyColor = givesKeyColor;
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new DungeonTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new FinalDungeonLevel(model, random, new PurpleRuinsTheme()));
        DungeonLevel level;
        do {
            PitfallDungeonConfig config = new PitfallDungeonConfig(new PurpleRuinsTheme(), new UndeadMonsterFactory());
            config.addRequiredDeadEndObject(new FatueKeyObject(givesKeyColor), FatueKeyObject.PREVALENCE);
            level = new DungeonLevel(model, random, false, 7, config);
            System.err.println("Key not spawned in east wing, retrying");
            if (config.allRequiredObjectsPlaced()) {
                break;
            }
        } while (true);
        levels.add(level);
        DungeonLevelConfig config2 = new NoLeversDungeonConfig(new PurpleBrickTheme(), new UndeadMonsterFactory());
        levels.add(new DungeonLevel(model, random, false, 7, config2));

        RuinsDungeon dungeon = new RuinsDungeon(levels);
        DungeonLevel bottomLevel = dungeon.getLevel(dungeon.getNumberOfLevels()-1);
        DungeonRoom entryRoom = bottomLevel.getRoom(bottomLevel.getDescentPoint());
        entryRoom.setConnection(0, new TowerExit());

        FinalDungeonLevel firstLevel = (FinalDungeonLevel)dungeon.getLevel(0);
        FatueStaffRoom staffRoom = new FatueStaffRoom();
        staffRoom.setConnection(0, new StairsDown(new Point(1, 0)));
        firstLevel.setFinalRoom(staffRoom, false);
        return dungeon;
    }
}
