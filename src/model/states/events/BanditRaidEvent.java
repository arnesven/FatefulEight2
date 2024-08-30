package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.List;

public class BanditRaidEvent extends DailyEventState {
    public BanditRaidEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.BANDIT, "Bandits");
        println("This farmstead has been plagued by bandits for some time.");
        model.getParty().randomPartyMemberSay(model, List.of("It's time to teach this rabble a lesson."));
        runCombat(BanditEvent.generateBanditEnemies(model));
        setCurrentTerrainSubview(model);
        possiblyGetHorsesAfterCombat("bandits", 5);
        if (!haveFledCombat() && !model.getParty().isWipedOut()) {
            new GuestEvent(model).doEvent(model);
        }
    }

    @Override
    protected boolean isFreeRations() {
        return !haveFledCombat();
    }
}
