package model.states.events;

import model.Model;
import model.enemies.BatEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class BatsEvent extends DailyEventState {
    public BatsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "What's that fluttering sound?");
        model.getParty().randomPartyMemberSay(model, List.of("Bats!"));
        List<Enemy> enemies = new ArrayList<>();
        int numberOfBats = MyRandom.randInt(6, 12);
        for (int i = 0; i < numberOfBats; ++i) {
            enemies.add(new BatEnemy('A'));
        }
        runCombat(enemies);
    }
}
