package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.*;
import model.ruins.themes.GrayBrickTheme;

import java.util.Random;
import java.util.Set;

public class TallSpireDungeonConfig extends DungeonLevelConfig {

    private static final double CHEST_PREVALENCE = 0.4;
    private static final double MONSTER_PREVALENCE = 0.1;
    private static final double TRAP_PREVALENCE = 0.2;
    private static final double NO_LEVERS = 0.0;
    private static final double NO_CORPSES = 0.0;
    private static final double NO_SPIKE_TRAPS = 0.0;
    private static final double NO_CAMPFIRES = 0.0;
    private static final double NO_CORRIDORS = 0.0;

    public TallSpireDungeonConfig() {
        super(new GrayBrickTheme(), new MonsterFactory(),
                CHEST_PREVALENCE, NO_LEVERS, NO_CORPSES, MONSTER_PREVALENCE,
                LOCKED_DOOR_PREVALENCE, NO_SPIKE_TRAPS, TRAP_PREVALENCE, NO_CAMPFIRES, NO_CORRIDORS);
    }

    @Override
    protected void addDecorations(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random, MonsterFactory monsterFactory) {
        addWindows(dungeonLevel);
    }

    private void addWindows(DungeonLevel dungeonLevel) {
        DungeonRoom startingRoom = dungeonLevel.getRoom(dungeonLevel.getStartingPoint());
        DungeonRoom endingRoom = dungeonLevel.getRoom(dungeonLevel.getDescentPoint());
        DungeonRoom[][] matrix = dungeonLevel.getRooms();
        for (int x = 0; x < matrix.length; ++x) {
            DungeonRoom room = matrix[x][0];
            if (room != null && room != startingRoom && room != endingRoom) {
                room.addDecoration(new LargeWindow(1, 0, false));
                room.addDecoration(new LargeWindow(2, 0, false));
            }
        }

    }
}
