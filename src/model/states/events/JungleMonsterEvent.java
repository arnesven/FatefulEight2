package model.states.events;

import model.Model;
import model.enemies.Enemy;
import util.MyRandom;

import java.util.List;

public class JungleMonsterEvent extends RoamingMonsterEvent{
    public JungleMonsterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean ambush = MyRandom.flipCoin();
        println("The party " + (ambush?"is ambushed by":"encounters") + " some monsters roaming the jungle!");
        List<Enemy> enemies = null;
        int roll = MyRandom.rollD10();
        switch (roll) {
            case 1:
                enemies = makeBatEnemies();
                break;
            case 2:
                enemies = makeSpiderEnemies();
                break;
            case 3:
            case 4:
                enemies = makeLizardmen();
                break;
            case 5:
                enemies = makeBearEnemies();
                break;
            case 6:
                enemies = makeCrocodileEnemies();
                break;
            case 7:
            case 8:
                enemies = makeFrogmenEnemies();
                break;
            case 9:
                enemies = makeViperEnemies();
                break;
            case 10:
                enemies = makeTrollEnemies();
        }
        if (!ambush) {
            runCombat(enemies);
        } else {
            runAmbushCombat(enemies, model.getCurrentHex().getCombatTheme(), true);
        }
    }
}
