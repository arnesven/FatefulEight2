package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.SkeletonEnemy;
import model.states.DailyEventState;
import view.combat.DungeonTheme;

import java.util.ArrayList;
import java.util.List;

public class SkeletonCryptEvent extends DailyEventState {
    public SkeletonCryptEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party encounters some skeletons!");
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new SkeletonEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new SkeletonEnemy('A'));
        }
        runCombat(result, new DungeonTheme(), true);
    }
}
