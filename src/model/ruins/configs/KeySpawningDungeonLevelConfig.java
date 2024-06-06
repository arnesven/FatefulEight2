package model.ruins.configs;

import model.ruins.objects.FatueKeyObject;
import model.ruins.factories.MonsterFactory;
import model.ruins.themes.DungeonTheme;
import view.MyColors;

public class KeySpawningDungeonLevelConfig extends DungeonLevelConfig {

    public KeySpawningDungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory, MyColors keyColor, boolean spawnOtherObjects) {
        super(theme, monsterFactory);
        addRequiredDeadEndObject(new FatueKeyObject(keyColor), FatueKeyObject.PREVALENCE);
    }

    public boolean isKeySpawned() {
        return allRequiredObjectsPlaced();
    }
}
