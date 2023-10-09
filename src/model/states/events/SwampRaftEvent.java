package model.states.events;

import model.Model;
import model.map.Direction;
import model.states.DailyEventState;
import view.sprites.Sprite;

import java.awt.*;

public class SwampRaftEvent extends RaftEvent {
    private LostEvent innerEvent = null;

    public SwampRaftEvent(Model model) {
        super(model, "traverse the swamp");
    }

    @Override
    protected boolean eventIntro(Model model) {
        if (model.getCurrentHex().getRivers() == Direction.NONE) {
            this.innerEvent = new LostEvent(model);
            this.innerEvent.doEvent(model);
            return false;
        }
        return super.eventIntro(model);
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return super.haveFledCombat();
    }
}
