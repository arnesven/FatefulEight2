package model.states.events;

import model.Model;
import model.enemies.SnowyBeastEnemy;
import model.states.DailyEventState;
import view.subviews.TundraCombatTheme;

import java.util.List;

public class SnowyBeastEvent extends DailyEventState {
    public SnowyBeastEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("This beast is covered in white fur. It has tusks, " +
                "horns and a terrible temper. It is coming straight for the " +
                "party.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Everybody, get ready to fight!");
        runCombat(List.of(new SnowyBeastEnemy('A')), new TundraCombatTheme(), true);
    }
}
