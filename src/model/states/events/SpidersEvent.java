package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.SpiderEnemy;
import model.states.DailyEventState;

import java.util.List;

public class SpidersEvent extends DailyEventState {
    public SpidersEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("An unnerving chittering echoes around the party " +
                "members. Giant spiders are surrounding them. These " +
                "devil-spawn have poisonous attacks that paralyze their " +
                "victims.");
        runCombat(makeSpiders());
    }

    public static List<Enemy> makeSpiders() {
        return List.of(new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A'), new SpiderEnemy('A'));
    }
}
