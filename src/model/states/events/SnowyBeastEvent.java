package model.states.events;

import model.Model;
import model.enemies.SnowyBeastEnemy;
import model.states.DailyEventState;

import java.util.List;

public class SnowyBeastEvent extends DailyEventState {
    public SnowyBeastEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a large animal, a beast with white fur";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Snowy Beast", "This beast is covered in white fur. It has tusks, " +
                "horns and a terrible temper. It is coming straight for the " +
                "party.");
        leaderSay("Everybody, get ready to fight!");
        runCombat(List.of(new SnowyBeastEnemy('A')));
    }
}
