package model.states.events;

import model.Model;
import model.enemies.CobraEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;
import view.combat.NightGrassCombatTheme;

import java.util.List;

public class CobraNightAttackEvent extends NightTimeAttackEvent {
    public CobraNightAttackEvent(Model model) {
        super(model, 9, "notices a large snake slithering into the camp",
                new NightGrassCombatTheme(), "a large snake has slithered into the camp");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        return List.of(new CobraEnemy('A'));
    }
}
