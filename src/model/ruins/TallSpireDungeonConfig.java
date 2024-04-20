package model.ruins;

import model.ruins.objects.DungeonChest;
import model.ruins.objects.DungeonPitfallTrap;
import model.ruins.objects.DungeonSpikeTrap;
import model.ruins.objects.MonsterFactory;
import model.ruins.themes.GrayBrickTheme;

import java.util.Random;

public class TallSpireDungeonConfig extends DungeonLevelConfig {

    private static final double CHEST_PREVALENCE = 0.4;
    private static final double MONSTER_PREVALENCE = 0.1;
    private static final double TRAP_PREVALENCE = 0.2;

    public TallSpireDungeonConfig() {
        super(new GrayBrickTheme(), new MonsterFactory());
    }

    protected void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        if (roll < CHEST_PREVALENCE) {
            room.addObject(new DungeonChest(random));
        }
    }

    protected void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < MONSTER_PREVALENCE) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < MONSTER_PREVALENCE + TRAP_PREVALENCE) {
            DungeonPitfallTrap.makePitfallTrap(room, random);
        }
    }
}
