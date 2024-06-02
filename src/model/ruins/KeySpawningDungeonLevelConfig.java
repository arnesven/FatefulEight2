package model.ruins;

import model.ruins.objects.FatueKeyObject;
import model.ruins.objects.MonsterFactory;
import model.ruins.themes.DungeonTheme;

import java.util.Random;

public class KeySpawningDungeonLevelConfig extends DungeonLevelConfig {
    private static final double KEY_PREVALENCE = 0.33;
    private final FatueKeyObject keyObject;
    private boolean keySpawned = false;

    public KeySpawningDungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory, FatueKeyObject keyObject) {
        super(theme, monsterFactory);
        this.keyObject = keyObject;
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < KEY_PREVALENCE && !keySpawned) {
            room.addObject(keyObject);
            keySpawned = true;
        } else {
            super.addDeadEndObject(dungeonLevel, room, random);
        }
    }

    public boolean isKeySpawned() {
        return keySpawned;
    }

}
