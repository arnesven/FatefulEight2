package model.states.events;

import model.Model;
import model.enemies.ManticoreEnemy;
import model.states.DailyEventState;
import view.subviews.DesertCombatTheme;

import java.util.List;

public class ManticoreEvent extends DailyEventState {
    public ManticoreEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("Is that a lion? No, it has wings! And what's that tail?"));
        println("This horrific beast suddenly swoops down on the party members.");
        runCombat(List.of(new ManticoreEnemy('A')));
    }
}
