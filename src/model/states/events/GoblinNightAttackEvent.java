package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;
import view.combat.MountainNightCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class GoblinNightAttackEvent extends NightTimeAttackEvent {
    public GoblinNightAttackEvent(Model model) {
        super(model, 7, "can spot goblins approaching the camp",
                new MountainNightCombatTheme(), "the camp is swarming with goblins");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        GoblinEnemy goblin = makeGoblin();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (goblin).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(makeGoblin());
        }
        return enemies;
    }

    private GoblinEnemy makeGoblin() {
        return MyRandom.sample(List.of(new GoblinSpearman('A'), new GoblinAxeWielder('A'),
                new GoblinBowman('A'), new GoblinWolfRiderEnemy('A')));
    }
}
