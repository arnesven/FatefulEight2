package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.factories.FatueGardenMonsterFactory;
import model.ruins.objects.FatueKeyObject;
import view.MyColors;

import java.util.Random;

public class CourtyardGardenDungeonLevelConfig extends GardenDungeonLevelConfig {
    private final KeySpawningDungeonLevelConfig keySpawner;

    public CourtyardGardenDungeonLevelConfig(FatueGardenMonsterFactory fatueGardenMonsterFactory, MyColors keyColor) {
        super(fatueGardenMonsterFactory);
        this.keySpawner = new KeySpawningDungeonLevelConfig(getTheme(),
                fatueGardenMonsterFactory, keyColor, false);
    }

    @Override
    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        if (!keySpawner.isKeySpawned()) {
            keySpawner.addDeadEndObject(dungeonLevel, room, random);
            if (keySpawner.isKeySpawned()) {
                return;
            }
        }
        super.addDeadEndObject(dungeonLevel, room, random);
    }

    public boolean keySpawned() {
        return keySpawner.isKeySpawned();
    }
}
