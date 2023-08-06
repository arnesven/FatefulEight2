package model.states.events;

import model.Model;
import model.states.EveningState;
import model.states.GameState;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public class EveningAtSeaState extends EveningState {
    public static SubView subViewShip = new ImageSubView("ship", "EVENING", "You spend the evening on the boat.");

    public EveningAtSeaState(Model model) {
        super(model, false, true, false);
    }

    @Override
    public void setSubView(Model model) {
        CollapsingTransition.transition(model, subViewShip);
    }

    @Override
    protected GameState nextState(Model model) {
        return new TravelByCharteredBoat(model);
    }

    @Override
    protected void checkForQuest(Model model) { }

    @Override
    protected void locationSpecificEvening(Model model) {
        model.getParty().partyMemberSay(model, model.getParty().getRandomPartyMember(),
                List.of("This sea air is good for my lungs.", "What a lovely voyage",
                        "The waves will lull me to sleep tonight.", "I hear seagulls...",
                        "I love the ocean.<3", "It'll be good when we get back on dry land again.",
                        "I feel seasick...#", "Salty and cold..."));
        model.getLog().waitForAnimationToFinish();
        super.locationSpecificEvening(model);
    }
}
