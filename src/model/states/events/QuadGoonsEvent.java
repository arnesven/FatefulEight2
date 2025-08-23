package model.states.events;

import model.Model;
import model.enemies.*;
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
        enemies.add(new QuadGoonMedic('B'));
        enemies.add(new QuadGoonMesmer('C'));
        enemies.add(new QuadGoonJuggernaught('D'));
        enemies.add(new QuadGoonHunter('E'));
        runCombat(enemies);
    }
}
