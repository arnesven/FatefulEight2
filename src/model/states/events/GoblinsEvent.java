package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class GoblinsEvent extends DailyEventState {
    public GoblinsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < MyRandom.randInt(1, 5); ++i) {
            enemies.add(new GoblinSwordsman('A'));
        }
        for (int i = 0; i < MyRandom.randInt(1, 5); ++i) {
            enemies.add(new GoblinSpearman('B'));
        }
        for (int i = 0; i < MyRandom.randInt(1, 5); ++i) {
            enemies.add(new GoblinAxeWielder('C'));
        }
        runCombat(enemies);
    }
}
