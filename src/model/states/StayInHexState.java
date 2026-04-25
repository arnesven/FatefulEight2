package model.states;

import model.Model;

public class StayInHexState extends GameState {
    public StayInHexState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        DailyEventState event = model.getCurrentHex().generateEvent(model);
        event.setQuestIntrosEnabled(true);
        return event.run(model);
    }
}
