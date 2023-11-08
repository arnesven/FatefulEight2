package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.OctopusEnemy;
import model.states.DailyEventState;

import java.util.List;

public class SeaMonsterEvent extends DailyEventState {
    public SeaMonsterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("Suddenly the water around the ship starts to bubble violently. In the next moment, large tentacles " +
                "burst out of the water and grab on to the ship. A huge octopus-like creature heaves itself onto the deck.");
        leaderSay("It's a... it's a... KRAKEN!");
        print("Press enter to continue.");
        waitForReturn();
        runCombat(OctopusEnemy.makeEnemyList());
    }
}
