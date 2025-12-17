package model.states.events;

import model.Model;
import model.classes.Classes;
import model.enemies.Enemy;
import model.states.DailyEventState;

import java.util.List;

public class BanditRaidEvent extends DailyEventState {
    public BanditRaidEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a group of people, looks like bandits";
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.BANDIT, "Bandits");
        showEventCard("Bandit Raid", "This farmstead has been plagued by bandits for some time.");
        model.getParty().randomPartyMemberSay(model, List.of("It's time to teach this rabble a lesson."));
        List<Enemy> enemies = BanditEvent.generateBanditEnemies(model);
        runCombat(enemies);
        setCurrentTerrainSubview(model);
        possiblyGetHorsesAfterCombat("bandits", enemies.size() + 1);
        if (!haveFledCombat() && !model.getParty().isWipedOut()) {
            new GuestEvent(model).doEvent(model);
        }
    }

    @Override
    protected boolean isFreeRations() {
        return !haveFledCombat();
    }
}
