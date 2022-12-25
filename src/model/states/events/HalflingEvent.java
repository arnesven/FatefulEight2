package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.Collections;
import java.util.List;

public class HalflingEvent extends DailyEventState {
    public HalflingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        print("The party encounters a halfling. This particular halfling is a");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 2) {
            println(" thief.");
            // TODO: implement thief event.
        } else if (dieRoll <= 6) {
            println(" farmer who completely ignores the party.");
        } else if (dieRoll <= 7) {
            print(" nobleman. ");
            NoblemanEvent noblemanEvent = new NoblemanEvent(model);
            noblemanEvent.doEvent(model);
        } else if (dieRoll <= 9) {
            println(" bard.");
            // TODO: implement bard event.
        } else if (dieRoll <= 10) {
            adventurerWhoMayJoin(model, Race.HALFLING);
        }
    }
}
