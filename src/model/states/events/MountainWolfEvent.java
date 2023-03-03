package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import model.states.DailyEventState;
import view.subviews.CombatTheme;
import view.subviews.MountainCombatTheme;

public class MountainWolfEvent extends WolfEvent {
    public MountainWolfEvent(Model model) {
        super(model);
    }

    @Override
    protected String getExtraText() {
        return " These wolves seem bigger and more ferocious than the ones you've seen before.";
    }

    @Override
    protected Enemy getWolf() {
        return new WolfEnemy('A', 6, 4);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new MountainCombatTheme();
    }
}
