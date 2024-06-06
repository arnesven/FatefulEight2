package model.ruins.configs;

import model.ruins.factories.GardenMonsterFactory;
import model.ruins.factories.MonsterFactory;
import model.ruins.themes.GardenDungeonTheme;


public class GardenDungeonLevelConfig extends DungeonLevelConfig {

    private static final double CHEST_PREVALENCE = 0.2;
    private static final double LEVER_PREVALENCE = 0.35;
    private static final double MONSTER_PREVALENCE = 0.2;
    private static final double LOCKED_DOOR_PREVALENCE = 0.0;
    private static final double CAMPFIRE_PREVALENCE = 0.2;
    private static final double NO_TRAPS = 0.0;
    private static final double NO_CORPSES = 0.0;

    public GardenDungeonLevelConfig(MonsterFactory monsterFactory) {
        super(new GardenDungeonTheme(), monsterFactory,
                CHEST_PREVALENCE, LEVER_PREVALENCE, NO_CORPSES,
                MONSTER_PREVALENCE, LOCKED_DOOR_PREVALENCE,
                NO_TRAPS, NO_TRAPS, CAMPFIRE_PREVALENCE);
    }

    public GardenDungeonLevelConfig() {
        this(new GardenMonsterFactory());
    }
}
