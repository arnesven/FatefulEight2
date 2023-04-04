package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class VeteranEvent extends DailyEventState {
    private final boolean withIntro;

    public VeteranEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public VeteranEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.None, "Veteran");
        if (withIntro) {
            print("The party passes an old tattered hut. Inside sits a venerable figure, who claims to be a veteran of the wars of old. ");
        }
        print("The veteran asks for some food. ");
        if (model.getParty().getFood() < 3) {
            println(" but you are embarrassed to admit you have none you can spare.");
            println("Veteran: \"Times are rough, but somehow I'll survive.\"");
            return;
        }

        print("Will you hand over 3 rations? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addToFood(-3);
            println("Veteran: \"Much obliged kind sir.\"");
            println("Veteran: \"I've served in several units. First I belonged to " +
                    "a unit of regulars, and later I was trained as a marksman.\"");
            print("The veteran is offering to train you in the ways of being a marksman ");
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.MAR);
            change.areYouInterested(model);
            println("Veteran: \"In the last war, I. was the most experienced soldier of my unit, " +
                    "so I was promoted to captain.\"");
            print("The veteran is offering to train you in the ways of being a captain ");
            change = new ChangeClassEvent(model, Classes.CAP);
            change.areYouInterested(model);
        } else {
            println("Veteran: \"Times are rough, but somehow I'll survive.\"");
        }
        println("You part ways with the veteran.");
    }
}
