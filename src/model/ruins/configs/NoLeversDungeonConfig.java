package model.ruins.configs;

import model.ruins.factories.MonsterFactory;
import model.ruins.themes.DungeonTheme;


public class NoLeversDungeonConfig extends DungeonLevelConfig {
    public NoLeversDungeonConfig(DungeonTheme theme, MonsterFactory monsterFactory) {
        super(theme, monsterFactory);
        super.setLeverPrevalence(0.0);
    }
}
