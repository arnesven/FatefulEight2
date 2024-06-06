package model.states.fatue;

import model.Model;
import model.items.special.FashionableSash;
import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.PitfallDungeonConfig;
import model.ruins.factories.UndeadMonsterFactory;
import model.ruins.objects.HiddenChestObject;
import model.ruins.objects.StairsDown;
import model.ruins.objects.TowerExit;
import model.ruins.themes.BlueBrickTheme;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.MansionTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FatueKeepNode extends KeyRequiredFatueDungeonNode {
    private static final String PROMPT = "This way leads to the inner keep of the fortress. Do you want to enter it?";

    public FatueKeepNode(MyColors requiresKey) {
        super("Enter Keep", false, requiresKey, PROMPT);
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new MansionTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new FinalDungeonLevel(model, random, new BlueBrickTheme()));
        DungeonLevel level;
        do {
            PitfallDungeonConfig config = new PitfallDungeonConfig(new BlueBrickTheme(), new UndeadMonsterFactory());
            config.addRequiredDeadEndObject(new HiddenChestObject(new FashionableSash()), 0.33); // TODO: Change to another item.
            level = new DungeonLevel(model, random, false, 7, config);
            System.err.println("Hidden chest not spawned in north tower, retrying");
            if (config.allRequiredObjectsPlaced()) {
                break;
            }
        } while (true);
        levels.add(level);

        DungeonLevelConfig config2 = new PitfallDungeonConfig(new BlueBrickTheme(), new UndeadMonsterFactory());
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
