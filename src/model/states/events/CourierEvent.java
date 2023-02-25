package model.states.events;

import model.Model;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;


public class CourierEvent extends DailyEventState {
    private final boolean withIntro;

    public CourierEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public CourierEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        List<UrbanLocation> list = model.getWorld().getLordLocations();
        if (model.getParty().getSummons().size() == list.size()) {
            new NoEventState(model).run(model);
            return;
        }

        UrbanLocation destination;
        do {
            destination = MyRandom.sample(list);
        } while (model.getParty().getSummons().containsKey(destination.getPlaceName()));

        if (withIntro) {
            println("A courier catches up to you and asks you to stop while he catches his breath.");
        }
        println("\"'" + model.getParty().getLeader().getFullName() + "'s Company' - that's you right? I have a letter for you.\"");
        println("He hands you a letter which reads: \"You have been summoned by the honorable " + destination.getLordName() + " to " +
                destination.getPlaceName() + ".\"");
        println("That's all the letter says. You stare blankly at it and then look at the messenger.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Do you know what this is about?");
        println("\"Sorry, I'm just a messenger.\" And he quickly takes off in the same direction from which he came.");
        println("You put the letter in your pocket and continue on your journey.");
        model.getParty().addSummon(destination);
    }
}
