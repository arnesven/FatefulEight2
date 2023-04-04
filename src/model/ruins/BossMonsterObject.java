package model.ruins;

import model.enemies.ArchDaemonEnemy;
import model.enemies.Enemy;
import model.enemies.VampireLordEnemy;
import model.enemies.WarlockEnemy;
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
            case 3:
                return new ArchDaemonEnemy('A');
            case 4:
            case 5: // TODO: make more bosses.
            case 6:
                return new WarlockEnemy('A');
            default:
                return new VampireLordEnemy('A');
        }

    }

    @Override
    protected boolean canSleep() {
        return false;
    }
}
