package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.objects.FatueKeyObject;
import model.ruins.factories.MonsterFactory;
import model.ruins.themes.DungeonTheme;
import view.MyColors;

import java.util.Random;

public class KeySpawningDungeonLevelConfig extends DungeonLevelConfig {
    private static final double KEY_PREVALENCE = 0.33;
    private final FatueKeyObject keyObject;
    private final boolean spawnOtherObjects;
    private boolean keySpawned = false;

    public KeySpawningDungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory, MyColors keyColor, boolean spawnOtherObjects) {
        super(theme, monsterFactory);
        this.keyObject = new FatueKeyObject(keyColor);
        this.spawnOtherObjects = spawnOtherObjects;
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < KEY_PREVALENCE && !keySpawned) {
            room.addObject(keyObject);
            keySpawned = true;
        } else if (spawnOtherObjects) {
            super.addDeadEndObject(dungeonLevel, room, random);
        }
    }

    public boolean isKeySpawned() {
        return keySpawned;
    }

}
