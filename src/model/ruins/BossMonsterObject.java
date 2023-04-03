package model.ruins;

import model.enemies.VampireEnemy;
import model.enemies.VampireLordEnemy;

import java.awt.*;
import java.util.List;

public class BossMonsterObject extends DungeonMonster {
    public BossMonsterObject() {
        super(List.of(new VampireLordEnemy('A')));
        setInternalPosition(new Point(3, 5));
    }

    @Override
    protected boolean canSleep() {
        return false;
    }
}
