package model.ruins;

import model.Model;
import model.enemies.VampireEnemy;
import model.states.ExploreRuinsState;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class BossMonsterObject extends DungeonMonster {
    public BossMonsterObject() {
        super(List.of(new VampireEnemy('A')));
        setInternalPosition(new Point(3, 5));
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        exploreRuinsState.println("There is a boss here...");
        exploreRuinsState.waitForReturn();
        super.entryTrigger(model, exploreRuinsState);
    }
}
