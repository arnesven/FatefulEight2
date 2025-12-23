package model.states.events;

import model.Model;
import model.RecruitInfo;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class FriendEvent extends DailyEventState {
    public FriendEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "one person traveling alone";
    }

    @Override
    protected void doEvent(Model model) {
        String textStart = "One of the party members encounters an old friend. After speaking a bit, the friend ";
        List<GameCharacter> list = model.getAvailableCharactersOfRace(Race.ALL);
        if (model.getParty().size() == 8 || list.isEmpty()) {
            showEventCard("Friend", textStart + " sets own about its own affairs.");
        } else {
            showEventCard("Friend", textStart + " expresses a wish to join the party.");
            RecruitableCharacter candidate = new RecruitableCharacter(MyRandom.sample(list));
            candidate.setStartingGold(0);
            candidate.setInfo(RecruitInfo.name);
            List<RecruitableCharacter> rcList = new ArrayList<>(List.of(candidate));
            RecruitState.setRandomLevels(model, rcList);
            RecruitState recruit = new RecruitState(model, rcList);
            recruit.run(model);
        }
    }
}
