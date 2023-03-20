package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class BrokenWagonEvent extends SalvageEvent {
    public BrokenWagonEvent(Model model) {
        super(model, " broken down wagon", 5);
    }
}
