package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;

import java.util.Collections;
import java.util.List;

public abstract class DailyEventState extends GameState {
    public DailyEventState(Model model) {
        super(model);
    }

    @Override
    public final GameState run(Model model) {
        doEvent(model);
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        return new EveningState(model, isFreeLodging(), isFreeRations());
    }

    protected boolean isFreeRations() {
        return false;
    }

    protected boolean isFreeLodging() {
        return false;
    }

    protected abstract void doEvent(Model model);

    protected void adventurerWhoMayJoin(Model model, Race race) {
        List<GameCharacter> list = model.getAvailableCharactersOfRace(race);
        if (list.isEmpty()) {
            println("n old friend with whom you exchange a few stories. You then part ways.");
        } else {
            println("n adventurer who offers to join your party.");
            Collections.shuffle(list);
            while (list.size() > 1) {
                list.remove(0);
            }
            list.get(0).setRandomStartingClass();
            RecruitState recruitState = new RecruitState(model, list);
            recruitState.run(model);
        }
    }
}
