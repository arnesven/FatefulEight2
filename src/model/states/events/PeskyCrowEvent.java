package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAdvantage;
import model.combat.abilities.CombatAction;
import model.enemies.*;
import model.states.DailyEventState;
import model.states.GameState;
import view.combat.MansionTheme;

import java.util.ArrayList;
import java.util.List;

public class PeskyCrowEvent extends DailyEventState {
    public PeskyCrowEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        printQuote("Pesky Crow", "Caw caw!");
        showEventCard("Pesky Crow",
                "You suddenly notice that a crow has been following you for some time. At first you " +
                "try to shoo it away but it keeps coming closer and closer like it's drawn to you.");
        model.getParty().randomPartyMemberSay(model,
                List.of("Shoo shoo!", "Scram bird!", "Just go away.",
                "I'm not feeding you.", "What do you want?"));
        println("Suddenly the crow attacks you! Press enter to continue.");
        waitForReturn();
        runCombat(List.of(new CrowEnemy('A')));
    }

}
