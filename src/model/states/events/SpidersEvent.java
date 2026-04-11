package model.states.events;

import model.Model;
import model.enemies.CommonSpiderEnemy;
import model.enemies.Enemy;
import model.enemies.ForestSpiderEnemy;
import model.enemies.SpiderEnemy;
import model.states.DailyEventState;

import java.util.List;

public class SpidersEvent extends DailyEventState {
    public SpidersEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "some large creatures, it's spiders";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("An unnerving chittering echoes around the party " +
                "members. Giant spiders are surrounding them. These " +
                "devil-spawn have poisonous attacks that paralyze their " +
                "victims.");
        runCombat(makeSpiders(new ForestSpiderEnemy('A')));
    }

    public static List<Enemy> makeSpiders(SpiderEnemy sp) {
        return List.of(sp.copy(), sp.copy(), sp.copy(), sp.copy());
    }
}
