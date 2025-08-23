package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.QuadGoonImolator;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class QuadGoonsEvent extends DailyEventState {
    public QuadGoonsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new QuadGoonImolator('A'));
        runCombat(enemies);
    }
}
