package model.ruins;

import model.ruins.objects.*;
import model.ruins.themes.DungeonTheme;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

public class DungeonLevelConfig implements Serializable {

    private final DungeonTheme theme;
    private final MonsterFactory monsterFactory;

    private static final double CHEST_PREVALENCE = 0.4;
    private static final double LEVER_PREVALENCE = 0.3;
    private static final double CORPSE_PREVALENCE = 0.3;
    private static final double MONSTER_PREVALENCE = 0.1;
    private static final double LOCKED_DOOR_PREVALENCE = 0.05;
    private static final double TRAP_PREVALENCE = 0.1;

    public DungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory) {
        this.theme = theme;
        this.monsterFactory = monsterFactory;
    }


    public void addContent(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        addDeadEndRoomObjects(dungeonLevel, visitedRooms, random);
        addJunctionRoomObjects(dungeonLevel, visitedRooms, random);
    }

    private void addJunctionRoomObjects(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        DungeonRoom startingRoom = dungeonLevel.getRoom(dungeonLevel.getStartingPoint());
        DungeonRoom endingRoom = dungeonLevel.getRoom(dungeonLevel.getDescentPoint());
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() > 1) {
                if (startingRoom != room && endingRoom != room) {
                    addJunctionObject(room, random, monsterFactory);
                }
            }
        }
    }

    private void addDeadEndRoomObjects(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() == 1) {
                addDeadEndObject(dungeonLevel, room, random);
            }
        }
    }

    protected void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < MONSTER_PREVALENCE) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < MONSTER_PREVALENCE + TRAP_PREVALENCE) {
            DungeonSpikeTrap.makeSpikeTrap(room, random);
        }
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < CHEST_PREVALENCE) {
            room.addObject(new DungeonChest(random));
        } else if (roll < CHEST_PREVALENCE + LEVER_PREVALENCE) {
            LeverObject lever = new LeverObject(random);
            room.addObject(lever);
            dungeonLevel.getRoom(dungeonLevel.getDescentPoint()).connectLeverToDoor(lever);
        } else if (roll < CHEST_PREVALENCE + LEVER_PREVALENCE + CORPSE_PREVALENCE) {
            room.addObject(new CorpseObject());
        }
    }


    public double getLockedDoorPrevalence() {
        return LOCKED_DOOR_PREVALENCE;
    }

    public DungeonTheme getTheme() {
        return theme;
    }
}
