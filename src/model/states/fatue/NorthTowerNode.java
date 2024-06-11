package model.states.fatue;

import model.Model;
import model.items.clothing.MytheriumArmor;
import model.items.special.FashionableSash;
import model.items.special.FatueKeyItem;
import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.NoLeversDungeonConfig;
import model.ruins.configs.PitfallDungeonConfig;
import model.ruins.factories.FatueTowerMonsterFactory;
import model.ruins.factories.UndeadMonsterFactory;
import model.ruins.objects.HiddenChestObject;
import model.ruins.objects.StairsDown;
import model.ruins.objects.TowerExit;
import model.ruins.themes.PurpleBrickTheme;
import model.ruins.themes.PurpleRuinsTheme;
import model.ruins.themes.RedBrickTheme;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NorthTowerNode extends KeyRequiredFatueDungeonNode {
    private static final String PROMPT = "This way leads to the structure which looms high " +
            "over the rest of the fortress, the North Tower. Do you enter?";
    private final FatueKeyItem key1;
    private final FatueKeyItem key2;

    public NorthTowerNode(MyColors requiredKey1, MyColors requiredKey2) {
        super("North Tower", false, requiredKey1, PROMPT);
        this.key1 = new FatueKeyItem(requiredKey1); // TODO: Other monster factory, and puzzle
        this.key2 = new FatueKeyItem(requiredKey2);
    }

    @Override
    protected boolean checkForKey(Model model) {
        return FatueKeyItem.hasKey(model, key1.getColor()) && FatueKeyItem.hasKey(model, key2.getColor());
    }

    @Override
    protected void printRequiredKeys(AdvancedDailyActionState state) {
        state.println("You need the " + key1.getName() + " and " + key2.getName() + " to open the door.");
    }

    @Override
    protected void printKeyUsed(AdvancedDailyActionState state) {
        state.println("You use the " + key1.getName() + " and "  + key2.getName() + " to unlock the door.");
    }

    @Override
    protected CombatTheme getCombatTheme() {
        return new DungeonTheme();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        levels.add(new FinalDungeonLevel(model, random, new RedBrickTheme()));
        DungeonLevel level;
        do {
            PitfallDungeonConfig config = new PitfallDungeonConfig(new RedBrickTheme(), new FatueTowerMonsterFactory());
            config.addRequiredDeadEndObject(new HiddenChestObject(new FashionableSash()), 0.33);
            level = new DungeonLevel(model, random, false, 4, config);
            System.err.println("Fashionable sash not spawned in north tower, retrying");
            if (config.allRequiredObjectsPlaced()) {
                break;
            }
        } while (true);
        levels.add(level);

        List<DungeonLevel> inBetweenLevels;
        do {
            inBetweenLevels = new ArrayList<>();
            DungeonLevelConfig config2 = new PitfallDungeonConfig(new RedBrickTheme(), new FatueTowerMonsterFactory());
            config2.addRequiredDeadEndObject(new HiddenChestObject(new MytheriumArmor()), 0.33);
            for (int i = 0; i < 8; i++) {
                inBetweenLevels.add(new DungeonLevel(model, random, false, 4, config2));
            }
            if (config2.allRequiredObjectsPlaced()) {
                break;
            }
            System.err.println("Mytherium armor not spawned. Retrying");
        } while (true);

        levels.addAll(inBetweenLevels);
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
