package model.states.fatue;

import model.Model;
import model.items.accessories.BootsOfMobility;
import model.items.special.FashionableSash;
import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.PitfallDungeonConfig;
import model.ruins.factories.UndeadMonsterFactory;
import model.ruins.objects.FatueKeyObject;
import model.ruins.objects.HiddenChestObject;
import model.ruins.objects.StairsDown;
import model.ruins.objects.TowerExit;
import model.ruins.themes.GrayBrickTheme;
import model.ruins.themes.GrayRuinsTheme;
import model.ruins.themes.RedBrickTheme;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EastTowerNode extends KeyRequiredFatueDungeonNode {
    private static final String PROMPT = "This way leads to the tall tower in the eastern corner of the fortress. " +
            "It has been ruined by the tooth of time. Do you want to go in?";
    private final MyColors givesKeyColor;

    public EastTowerNode(MyColors requiredKey, MyColors givesKey) {
        super("East Tower", false, requiredKey, PROMPT);
        this.givesKeyColor = givesKey;
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new DungeonTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new FinalDungeonLevel(model, random, new GrayRuinsTheme()));
        DungeonLevel level;
        do { // TODO: Other monster factory, and puzzle
            PitfallDungeonConfig config = new PitfallDungeonConfig(new GrayRuinsTheme(), new UndeadMonsterFactory());
            config.addRequiredDeadEndObject(new FatueKeyObject(givesKeyColor), FatueKeyObject.PREVALENCE);
            config.addRequiredDeadEndObject(new HiddenChestObject(new BootsOfMobility()), 0.33);
            level = new DungeonLevel(model, random, false, 4, config);
            System.err.println("Key not spawned in East tower, retrying");
            if (config.allRequiredObjectsPlaced()) {
                break;
            }
        } while (true);
        levels.add(level);
        for (int i = 0; i < 6; i++) {
            DungeonLevelConfig config2 = new PitfallDungeonConfig(new GrayBrickTheme(), new UndeadMonsterFactory());
            levels.add(new DungeonLevel(model, random, false, 4, config2));
        }

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
