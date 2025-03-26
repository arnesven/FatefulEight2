package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.PirateEnemy;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.combat.ShipCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class PirateShipEvent extends DailyEventState {
    public PirateShipEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("Suddenly the lookout calls out in alarm. He's spotted a pirate ship and it's gaining on you. Soon it's " +
                "clear that you will be boarded.");
        leaderSay("Everybody grab your gear, we're about to face some pirate scum!");
        print("Press enter to continue.");
        waitForReturn();
        List<Enemy> enemies = makePirateEnemies(model);
        runCombat(enemies, new ShipCombatTheme(), true);
    }

    public static List<Enemy> makePirateEnemies(Model model) {
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(model, new PirateEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new PirateEnemy('A'));
        }
        return result;
    }
}
