package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;

public class LumberMillEvent extends SimpleGeneralInteractionEvent {

    private final ChangeClassEvent changeClassEvent;
    private boolean freeLodge = false;

    public LumberMillEvent(Model model) {
        super(model, Classes.FOR, Race.randomRace(), "Lumberjack",
                "A large platform stands in the clearing ahead. Many huge logs lay all around " +
                        "and there is sawdust everywhere. As the party takes a short break the door to" +
                        " a nearby hut opens and a stocky fellow walks out and greets them.");
        changeClassEvent = new ChangeClassEvent(model, Classes.FOR);
    }

    @Override
    public String getDistantDescription() {
        return "a lumber mill";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to lumber mill", "There's " + getDistantDescription() + " nearby");
    }

    @Override
    protected boolean isFreeLodging() {
        return freeLodge;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        showEventCard("The lumberjack and invites the party into his home for the night. A good earthy stew" +
                " awaits and good beer and bread. Stories are shared and the lumberjack tells of " +
                "the many strange things that lay hidden in these parts of the forest.");
        print("The Lumberjack offers to train you in the ways of being a Forester, ");
        // FEATURE: Options: Cut some wood (gain materials)
        changeClassEvent.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, getPortrait(), "Lumberjack");
        this.freeLodge = true;
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a lumber jack. I cut down trees and turn them into planks.";
    }
}
