package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class NoblemanEvent extends DailyEventState {
    public NoblemanEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You share the evening with a nobleman and their " +
                "entourage. After telling the great story of the party's " +
                "exploits, the nobleman gladly gives you a small stipend.");
        showRandomPortrait(model, Classes.NOB, "Noble");
        int amount = 10 * model.getParty().size();
        model.getParty().addToGold(amount);
        println("The party receives " + amount + " gold from the nobleman.");
        model.getParty().partyMemberSay(model, MyRandom.sample(model.getParty().getPartyMembers()),
                List.of("Much obliged.", "This extra coin will come in handy.", "It's for a worthy cause.",
                        "A good deed sir.", "Perhaps we can pay you back one day...",
                        "How very generous of you!3"));
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.NOB);
        print("The nobleman also offers to inspire you to take on the high life, ");
        change.areYouInterested(model);
        println("You part ways with the nobleman.");
        model.getParty().partyMemberSay(model, MyRandom.sample(model.getParty().getPartyMembers()),
                List.of("What a sucker...", "Such a nice person.", "We should hang out with noblemen more often."));
    }
}
