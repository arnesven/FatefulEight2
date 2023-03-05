package model.states.events;

import model.Model;
import model.enemies.ScorpionEnemy;
import model.states.DailyEventState;
import view.subviews.DesertCombatTheme;

import java.util.List;

public class ScorpionEvent extends DailyEventState {
    public ScorpionEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A giant scorpion suddenly crawls out from under a large " +
                "rock.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Watch out, that tail has a poisoned stinger!");
        runCombat(List.of(new ScorpionEnemy('A')));
    }
}
