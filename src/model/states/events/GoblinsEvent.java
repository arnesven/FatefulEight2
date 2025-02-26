package model.states.events;

import model.Model;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class GoblinsEvent extends DailyEventState {
    private boolean fled = false;

    public GoblinsEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a throng of goblins";
    }

    @Override
    protected void doEvent(Model model) {
        print("A horde of goblins are coming straight for you! Do you turn and run? (Y/N) ");
        if (yesNoInput()) {
            this.fled = true;
        } else {
            List<Enemy> enemies = randomGoblins();
            runCombat(enemies);
        }
    }

    @Override
    public boolean haveFledCombat() {
        return fled || super.haveFledCombat();
    }

    public static List<Enemy> randomGoblins() {
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
        return enemies;
    }
}
