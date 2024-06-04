package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.objects.CorpseObject;
import model.ruins.objects.DungeonChest;
import model.ruins.factories.MonsterFactory;
import model.ruins.themes.DungeonTheme;

import java.util.Random;

public class NoLeversDungeonConfig extends DungeonLevelConfig {
    private static final double CHEST_PREVALENCE = 0.4;
    private static final double CORPSE_PREVALENCE = 0.3;

    public NoLeversDungeonConfig(DungeonTheme theme, MonsterFactory monsterFactory) {
        super(theme, monsterFactory);
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < CHEST_PREVALENCE) {
            room.addObject(new DungeonChest(random));
        } else if (roll < CHEST_PREVALENCE + CORPSE_PREVALENCE) {
            room.addObject(new CorpseObject());
        }
    }
}
