package model.ruins.configs;

import model.ruins.factories.FatueGardenMonsterFactory;
import model.ruins.objects.FatueKeyObject;
import view.MyColors;

public class CourtyardGardenDungeonLevelConfig extends GardenDungeonLevelConfig {
    public CourtyardGardenDungeonLevelConfig(FatueGardenMonsterFactory fatueGardenMonsterFactory, MyColors keyColor) {
        super(fatueGardenMonsterFactory);
        addRequiredDeadEndObject(new FatueKeyObject(keyColor), FatueKeyObject.PREVALENCE);
    }

    public boolean keySpawned() {
        return allRequiredObjectsPlaced();
    }
}
