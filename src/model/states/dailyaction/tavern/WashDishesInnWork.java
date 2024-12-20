package model.states.dailyaction.tavern;

import model.Model;

public class WashDishesInnWork extends InnWorkAction {
    public WashDishesInnWork() {
        super("Wash dishes", "We do have an enormous amount of dishes that need washing. Won't pay much though.");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        state.println("You spend the rest of the day cleaning up in the kitchen.");
        state.bartenderSay(model,  "Good work. Here's your pay.");
        model.getParty().addToGold(Math.min(4, model.getParty().size()));
        state.println("You got " + model.getParty().size() + " gold.");
    }
}
