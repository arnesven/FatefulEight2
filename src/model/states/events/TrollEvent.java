package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.TrollEnemy;
import model.states.DailyEventState;

import java.util.List;

public class TrollEvent extends DailyEventState {
    public TrollEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("Hey, that looks like a nice rest spot up ahead... Is that an old church?"));
        model.getParty().randomPartyMemberSay(model, List.of("The ruins of an old watch tower."));
        model.getParty().randomPartyMemberSay(model, List.of("Hold up, is that a troll?"));
        print("Do you attack the troll? (Y/N) ");
        if (yesNoInput()) {
            runCombat(List.of(new TrollEnemy('A')));
            if (!haveFledCombat()) {
                println("The watch tower does indeed provide a good resting place this evening.");
                println("Each character recovers 1 extra HP.");
                model.getParty().getPartyMembers().forEach((GameCharacter gc) -> gc.addToHP(1));
            }
        }
    }
}
