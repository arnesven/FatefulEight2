package model.states.events;

import model.Model;
import model.enemies.CrocodileEnemy;
import model.states.DailyEventState;

import java.util.List;

public class CrocodilesEvent extends DailyEventState {
    public CrocodilesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("As the party tries to cross a wetland by stepping on " +
                "floating logs they soon realize, they are not logs at all! " +
                "The creatures come alive and snap at you with deadly jaws.");
        runCombat(List.of(new CrocodileEnemy('A'), new CrocodileEnemy('A'), new CrocodileEnemy('A')), false);
    }
}
