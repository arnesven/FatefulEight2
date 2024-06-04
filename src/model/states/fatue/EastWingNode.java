package model.states.fatue;

import model.Model;
import model.characters.GameCharacter;
import model.items.special.FatueKeyItem;
import model.ruins.*;
import model.ruins.configs.DungeonLevelConfig;
import model.ruins.configs.NoLeversDungeonConfig;
import model.ruins.configs.PitfallDungeonConfig;
import model.ruins.factories.UndeadMonsterFactory;
import model.ruins.objects.*;
import model.ruins.themes.PurpleBrickTheme;
import model.ruins.themes.PurpleRuinsTheme;
import model.states.dailyaction.AdvancedDailyActionState;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EastWingNode extends FatueDungeonNode {
    private final FatueKeyItem requiredKey;

    public EastWingNode() {
        super("East Wing", false,
                "This hallway leads to the rundown eastern wing. " +
                        "It's dark and quiet this way, do you want to continue?");
        this.requiredKey = new FatueKeyItem(MyColors.DARK_RED);
    }

    @Override
    protected boolean runPreHook(Model model, AdvancedDailyActionState state) {
        state.println("You start down the hallway but soon encounter a locked door.");
        if (FatueKeyItem.hasKey(model, requiredKey.getColor())) {
            state.println("You use the " + requiredKey.getName() + " to unlock the door.");
            return true;
        }
        if (model.getParty().size() > 1) {
            state.leaderSay("Any chance to pick this lock?");
            GameCharacter rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            state.partyMemberSay(rando, "No can do. That's a masterpiece lock. The only " +
                    "thing that will open it is the proper key.");
        }
        state.println("You need the " + requiredKey.getName() + " to open the door.");
        return false;
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        List<DungeonLevel> levels = new ArrayList<>();
        Random random = new Random();
        DungeonLevelConfig config = new PitfallDungeonConfig(new PurpleRuinsTheme(), new UndeadMonsterFactory(model));
        levels.add(new FinalDungeonLevel(random, config.getTheme()));
        levels.add(new DungeonLevel(random, false, 7, config));
        config = new NoLeversDungeonConfig(new PurpleBrickTheme(), new UndeadMonsterFactory(model));
        levels.add(new DungeonLevel(random, false, 7, config));

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

    @Override
    protected CombatTheme getCombatTheme() {
        return new DungeonTheme();
    }

}
