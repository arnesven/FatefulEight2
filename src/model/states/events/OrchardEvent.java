package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class OrchardEvent extends DailyEventState {
    public OrchardEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party suddenly finds itself in a lovely orchard, " +
                "with fruit hanging everywhere. As one member of your " +
                "party reaches for a low hanging piece of fruit, a farmer " +
                "suddenly pops up behind a bush. Embarrassed, you " +
                "try to explain that you are lost and thought the trees " +
                "were wild. \"Don't worry!\" The farmer replies. \"I have " +
                "way more fruit than I can ever sell at the market. Take " +
                "as much as you want!\"");
        int avail = model.getParty().rationsLimit() - model.getParty().getFood();
        int gained = Math.min(avail, 5*model.getParty().size());
        println("The party gains " + gained + " rations.");
        model.getParty().randomPartyMemberSay(model, List.of("Yummy!3"));
    }
}
