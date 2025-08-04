package model.ruins.objects;

import model.enemies.*;
import util.MyRandom;

import java.awt.*;
import java.util.List;

public class BossMonsterObject extends DungeonMonster {
    public BossMonsterObject() {
        super(List.of(makeRandomBoss()));
        setInternalPosition(new Point(3, 5));
    }

    private static Enemy makeRandomBoss() {
        int dieRoll = MyRandom.rollD10();
        switch (dieRoll) {
            case 1:
            case 2:
                return new ArchDaemonEnemy('A');
            case 3:
            case 4:
                return new WarlockEnemy('A');
            case 5: // FEATURE: make more bosses.
            case 6:
                return new DoomMageEnemy('A');
            case 7:
            case 8:
                return DragonEnemy.generateDragon('A');
            default:
                return new VampireLordEnemy('A');
        }

    }

    @Override
    protected boolean canSleep() {
        return false;
    }
}
