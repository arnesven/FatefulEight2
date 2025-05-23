package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.Enemy;
import model.map.HexLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.AcceptTravellerState;
import model.travellers.Traveller;
import model.travellers.TravellerCompletionHook;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class MeetTravellerEvent extends GeneralInteractionEvent {
    private final GameCharacter traveller;
    private final ProvokedStrategy provokedStrategy;
    private final int extraReward;
    private final TravellerCompletionHook completionHook;

    public MeetTravellerEvent(Model model, GameCharacter traveller, int stealMoney,
                              ProvokedStrategy provokedStrategy, int extraReward, TravellerCompletionHook hook) {
        super(model, "Talk to", stealMoney);
        this.traveller = traveller;
        this.provokedStrategy = provokedStrategy;
        this.extraReward = extraReward;
        this.completionHook = hook;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("You meet a " + traveller.getName().toLowerCase() + " on the road.");
        showExplicitPortrait(model, traveller.getAppearance(), traveller.getName());
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        List<Point> path = getPathToDestination(model);
        HexLocation location = model.getWorld().getHex(path.get(path.size()-1)).getLocation();
        Traveller t = new Traveller(traveller.getName(), traveller.getAppearance(),
                location, path.size(), extraReward, completionHook);
        AcceptTravellerState accept = new AcceptTravellerState(model, this, t);
        accept.run(model);
        return !model.getParty().getActiveTravellers().contains(t);
    }

    protected abstract List<Point> getPathToDestination(Model model);

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return traveller;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return provokedStrategy;
    }
}
