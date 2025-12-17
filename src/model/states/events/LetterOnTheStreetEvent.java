package model.states.events;

import model.Model;
import model.states.DailyEventState;

public abstract class LetterOnTheStreetEvent extends DailyEventState {
    public LetterOnTheStreetEvent(Model model) {
        super(model);
    }

    protected abstract void innerDoEvent(Model model);

    @Override
    protected void doEvent(Model model) {
        showEventCard("Letter", "You are pacing through town when suddenly something on the ground catches your eye. At first, " +
                "you take it for just a piece of trash, but then you realize it's a letter. You pick it up.");
        innerDoEvent(model);
    }
}
