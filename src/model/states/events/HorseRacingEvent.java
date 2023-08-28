package model.states.events;

import model.Model;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;
import view.subviews.HorseRacingSubView;

public class HorseRacingEvent extends DailyEventState {
    public HorseRacingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        Horse horse = HorseHandler.generateHorse();
        if (model.getParty().getHorseHandler().size() > 0) {
            horse = model.getParty().getHorseHandler().get(0);
        }
        HorseRacingSubView subView = new HorseRacingSubView(model.getParty().getPartyMember(0), horse);
        model.setSubView(subView);
        waitForReturn();

    }
}
