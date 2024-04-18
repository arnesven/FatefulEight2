package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.List;

public class FriendEvent extends DailyEventState {
    public FriendEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        print("One of the party members encounters an old friend. After speaking a bit, the friend ");
        List<GameCharacter> list = model.getAvailableCharactersOfRace(Race.ALL);
        if (model.getParty().size() == 8 || list.isEmpty()) {
            println(" sets own about its own affairs.");
        } else {
            println(" expresses a wish to join the party.");
            list = List.of(MyRandom.sample(list));
            RecruitState recruit = new RecruitState(model, list);
            list.get(0).setLevel(2);
            list.get(0).setRandomStartingClass();
            recruit.setStartingGoldEnabled(false);
            recruit.run(model);
        }
    }
}
