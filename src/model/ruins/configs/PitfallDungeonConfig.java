package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.objects.BlackWindow;
import model.ruins.objects.DungeonPitfallTrap;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.FatueKeyObject;
import model.ruins.themes.DungeonTheme;
import view.MyColors;

import java.util.Random;
import java.util.Set;

public class PitfallDungeonConfig extends NoLeversDungeonConfig {

    private static final double MONSTER_PREVALENCE = 0.15;
    private static final double TRAP_PREVALENCE = 0.1;
    private final KeySpawningDungeonLevelConfig keySpawner;

    public PitfallDungeonConfig(DungeonTheme theme, MonsterFactory monsterFactory, MyColors keyColor) {
        super(theme, monsterFactory);
        this.keySpawner = new KeySpawningDungeonLevelConfig(theme, monsterFactory, new FatueKeyObject(keyColor));
    }

    protected void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < MONSTER_PREVALENCE) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < MONSTER_PREVALENCE + TRAP_PREVALENCE) {
            DungeonPitfallTrap.makePitfallTrap(room, random);
        }
    }

    @Override
    public void addContent(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        super.addContent(dungeonLevel, visitedRooms, random);
        addWindows(dungeonLevel);
    }

    private void addWindows(DungeonLevel dungeonLevel) {
        DungeonRoom startingRoom = dungeonLevel.getRoom(dungeonLevel.getStartingPoint());
        DungeonRoom endingRoom = dungeonLevel.getRoom(dungeonLevel.getDescentPoint());
        DungeonRoom[][] matrix = dungeonLevel.getRooms();
        for (int x = 0; x < matrix.length; ++x) {
            DungeonRoom room = matrix[x][0];
            if (room != null && room != startingRoom && room != endingRoom) {
                room.addDecoration(new BlackWindow(2, 0));
            }
        }

    }

    @Override
    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        if (!keySpawner.isKeySpawned()) {
            keySpawner.addDeadEndObject(dungeonLevel, room, random);
            if (keySpawner.isKeySpawned()) {
                return; // key spawned in this room
            }
        }
        super.addDeadEndObject(dungeonLevel, room, random);
    }

    public boolean keySpawned() {
        return keySpawner.isKeySpawned();
    }
}
