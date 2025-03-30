package model.mainstory;

import model.Model;
import model.map.UrbanLocation;

public class GetSupportFromLordEvent extends VisitLordEvent {
    private final UrbanLocation castle;
    private final GainSupportOfNeighborKingdomTask task;

    public GetSupportFromLordEvent(Model model, UrbanLocation location, GainSupportOfNeighborKingdomTask task) {
        super(model);
        this.castle = location;
        this.task = task;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
        portraitSay("What do you want?");
        leaderSay("Give me support.");
        portraitSay("Okay, here you go.");
        task.setCompleted(true);

    }
}
