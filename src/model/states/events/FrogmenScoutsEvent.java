package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.FrogmanLeaderEnemy;
import model.enemies.FrogmanScoutEnemy;
import model.states.DailyEventState;

import java.util.List;

public class FrogmenScoutsEvent extends DailyEventState {
    public FrogmenScoutsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() > 1) {
            GameCharacter talker = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            println("The party comes to a little clearing and takes a short break.");
            model.getParty().partyMemberSay(model, talker, List.of("Something feels strange."));
            leaderSay("Yeah... like...");
            model.getParty().partyMemberSay(model, talker, List.of("Like what?"));
            leaderSay("Like we're being watched!");
        }
        model.getLog().waitForAnimationToFinish();
        println("Suddenly, several frogmen burst out of the bushes around you. Armed with blowpipes, their eyes seemed " +
                "crazed and their leader barks some commands. Without any attempt att negotiation, the simply attack you!");
        runCombat(List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanLeaderEnemy('B'), new FrogmanScoutEnemy('A')));
    }
}
