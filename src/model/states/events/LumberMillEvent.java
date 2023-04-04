package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class LumberMillEvent extends DailyEventState {

    private final ChangeClassEvent changeClassEvent;

    public LumberMillEvent(Model model) {
        super(model);
        changeClassEvent = new ChangeClassEvent(model, Classes.FOR);
    }

    @Override
    protected boolean isFreeLodging() {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.FOR, "Lumberjack");
        println("A large platform stands in the clearing ahead. Many huge logs lay all around " +
                "and there is sawdust everywhere. As the party takes a short break the door to" +
                " a nearby hut opens and a stocky fellow walks out and greets them. He is a " +
                "lumberjack and invites the party into his home for the night. A good earthy stew" +
                " awaits and good beer and bread. Stories are shared and the lumberjack tells of " +
                "the many strange things that lay hidden in these parts of the forest.");
        print("The Lumberjack offers to train you in the ways of being a Forester, ");
        changeClassEvent.areYouInterested(model);

    }
}
