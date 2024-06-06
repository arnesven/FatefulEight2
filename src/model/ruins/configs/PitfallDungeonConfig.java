package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.objects.BlackWindow;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.FatueKeyObject;
import model.ruins.themes.DungeonTheme;
import view.MyColors;

import java.util.Random;
import java.util.Set;

public class PitfallDungeonConfig extends NoLeversDungeonConfig {

    private static final double MONSTER_PREVALENCE = 0.15;
    private static final double TRAP_PREVALENCE = 0.1;

    public PitfallDungeonConfig(DungeonTheme theme, MonsterFactory monsterFactory, MyColors keyColor) {
        super(theme, monsterFactory);
        setMonsterPrevalence(MONSTER_PREVALENCE);
        setPitfallTrapPrevalence(TRAP_PREVALENCE);
        addRequiredDeadEndObject(new FatueKeyObject(keyColor), FatueKeyObject.PREVALENCE);
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
                room.addDecoration(new BlackWindow(2, 0));
            }
        }

    }

    public boolean keySpawned() {
        return allRequiredObjectsPlaced();
    }
}
