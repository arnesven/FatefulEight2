package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.World;
import model.states.RunAwayState;
import util.MyLists;
import util.MyRandom;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RapidsEvent extends RiverEvent {
    public RapidsEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return true;
    }

    @Override
    protected void doRiverEvent(Model model) {
        println("The party wades out into the shallows but are soon swept " +
                "away by an undertow. The river takes them to some " +
                "rapids which bang them up nicely.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                List.of("Hang on gang!", "This might get rough!", "Waaaah!#"));
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToHP(-2));
        removeKilledPartyMembers(model, false);
        println("Press enter to continue.");
        waitForReturn();
        RunAwayState runAwayState = new RapidsRunAwayState(model);
        runAwayState.run(model);
    }

    private static class RapidsRunAwayState extends RunAwayState {
        public RapidsRunAwayState(Model model) {
            super(model);
        }

        @Override
        protected Point selectDirection(Model model, MapSubView mapSubView) {
            List<Point> result = new ArrayList<>();
            for (Point dir : mapSubView.getDirections(model)) {
                Point p = new Point(model.getParty().getPosition());
                model.getWorld().move(p, dir.x, dir.y);
                if (model.getWorld().getHex(p).getRivers() != 0) {
                    result.add(dir);
                }
            }
            return MyRandom.sample(result);
        }

        @Override
        protected boolean checkForRiding(Model model) {
            return false;
        }
    }
}
