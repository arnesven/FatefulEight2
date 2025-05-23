package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.MountainWolfEnemy;

public class MountainWolfEvent extends WolfEvent {
    public MountainWolfEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "some creatures, wolves";
    }

    @Override
    protected String getExtraText() {
        return " These wolves seem bigger and more ferocious than the ones you've seen before.";
    }

    @Override
    protected Enemy getWolf() {
        return new MountainWolfEnemy('A');
    }
}
