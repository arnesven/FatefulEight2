package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.EveningWithoutQuestState;
import model.states.GameOverState;

import java.util.List;

public class LostEvent extends DailyEventState {
    private DailyEventState innerEvent;

    public LostEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() > 1) {
            model.getParty().randomPartyMemberSay(model, List.of("This place looks familiar..."));
            model.getParty().randomPartyMemberSay(model, List.of("It's because we've been here before."));
            println("The party has lost its way in the wilderness and has made no progress today.");
            boolean didSay = randomSayIfPersonality(PersonalityTrait.forgiving, List.of(model.getParty().getLeader()),
                    "It's okay... we get to see the sights, and we're getting exercise!");
            randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                    "You need to pay better attention to the map " + model.getParty().getLeader().getFirstName() + ".");
        } else {
            println("You have lost your way in the wilderness and have made no progress today.");
        }
        new EveningWithoutQuestState(model, false, false, false).run(model);
        if (model.getParty().isWipedOut()) {
            return;
        }
        setCurrentTerrainSubview(model);
        innerEvent = model.getCurrentHex().generateEvent(model);
        innerEvent.run(model);
    }

    @Override
    public boolean haveFledCombat() {
        return innerEvent.haveFledCombat();
    }
}
