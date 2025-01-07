package model.tasks;

import model.Model;
import model.states.events.AssassinationEndingEvent;

public interface AssassinationEndingMaker {
    AssassinationEndingEvent makeEvent(Model m);
}
