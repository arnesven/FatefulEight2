package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class TundraMonsterEvent extends WastelandMonsterEvent {
    public TundraMonsterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters some monsters roaming the tundra!");
        List<Enemy> enemies = null;
        int roll = MyRandom.rollD10();
        switch (roll) {
            case 1:
            case 2:
                enemies = makeBearEnemies();
                break;
            case 3:
                enemies = makeTrollEnemies();
                break;
            case 4:
                enemies = makeNomadEnemies();
                break;
            case 5:
            case 6:
                enemies = makeWolfEnemies();
                break;
            case 7:
            case 8:
                enemies = makeBanditEnemies();
                break;
            case 9:
            case 10:
                enemies = makeSnowyBeasts();
                break;
        }
        runCombat(enemies);
    }
}
