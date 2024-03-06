package model.states.events;

import model.Model;
import model.enemies.*;
import util.MyRandom;

import java.util.List;

public class WastelandMonsterEvent extends RoamingMonsterEvent {
    public WastelandMonsterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters some monsters roaming the wasteland!");
        List<Enemy> enemies = null;
        int roll = MyRandom.rollD10();
        switch (roll) {
            case 1:
            case 2:
                enemies = makeOrcsAndGoblins();
                break;
            case 3:
                enemies = makeLizardmen();
                break;
            case 4:
            case 5:
                enemies = makeUndeadEnemies();
                break;
            case 6:
            case 7:
                enemies = makeWolfEnemies();
                break;
            case 8:
                enemies = makeBanditEnemies();
                break;
            case 9:
                enemies = makeScorpionEnemies();
                break;
            case 10:
                enemies = makeTrollEnemies();
        }
        runCombat(enemies);
    }
}
