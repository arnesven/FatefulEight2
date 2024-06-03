package model.ruins;

import model.ruins.objects.*;
import model.ruins.themes.GardenDungeonTheme;

import java.util.Random;


public class GardenDungeonLevelConfig extends DungeonLevelConfig {

    private static final double CHEST_PREVALENCE = 0.2;
    private static final double LEVER_PREVALENCE = 0.35;
    private static final double MONSTER_PREVALENCE = 0.2;
    private static final double LOCKED_DOOR_PREVALENCE = 0.0;
    private static final double CAMPFIRE_PREVALENCE = 0.2;

    public GardenDungeonLevelConfig(MonsterFactory monsterFactory) {
        super(new GardenDungeonTheme(), monsterFactory);
    }

    public GardenDungeonLevelConfig() {
        this(new GardenMonsterFactory());
    }

    protected void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < MONSTER_PREVALENCE) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < MONSTER_PREVALENCE + CAMPFIRE_PREVALENCE) {
            room.addObject(new CampfireDungeonObject());
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
        }
    }

    @Override
    public double getLockedDoorPrevalence() {
        return LOCKED_DOOR_PREVALENCE;
    }
}
