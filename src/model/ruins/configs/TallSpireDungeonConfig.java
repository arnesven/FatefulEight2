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

    public TallSpireDungeonConfig() {
        super(new GrayBrickTheme(), new MonsterFactory());
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < CHEST_PREVALENCE) {
            room.addObject(new DungeonChest(random));
        }
    }

    @Override
    public void addContent(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        super.addContent(dungeonLevel, visitedRooms, random);
        addWindows(dungeonLevel, visitedRooms, random);
    }

    private void addWindows(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
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

    protected void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < MONSTER_PREVALENCE) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < MONSTER_PREVALENCE + TRAP_PREVALENCE) {
            DungeonPitfallTrap.makePitfallTrap(room, random);
        }
    }
}