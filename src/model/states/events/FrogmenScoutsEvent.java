package model.states.events;

import model.Model;
import model.enemies.FrogmanScoutEnemy;
import model.states.DailyEventState;

import java.util.List;

public class FrogmenScoutsEvent extends DailyEventState {
    public FrogmenScoutsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes to a little clearing and takes a short break.");
        model.getParty().randomPartyMemberSay(model, List.of("Something feels strange."));
        leaderSay("Yeah... like...");
        model.getParty().randomPartyMemberSay(model, List.of("Like what?"));
        leaderSay("Like we're being watched!");
        model.getLog().waitForAnimationToFinish();
        runCombat(List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A')));
    }
}
