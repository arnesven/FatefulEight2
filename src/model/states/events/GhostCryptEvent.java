package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.states.DailyEventState;
import view.subviews.DungeonTheme;

import java.util.ArrayList;
import java.util.List;

public class GhostCryptEvent extends DailyEventState {
    public GhostCryptEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters some ghosts!");
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new GhostEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new GhostEnemy('A'));
        }
        runCombat(result, new DungeonTheme(), true);
    }
}
