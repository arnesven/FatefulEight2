package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.Direction;
import model.map.SeaHex;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.RunAwayState;
import model.states.TravelBySeaState;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TravelByCharteredBoat extends AlternativeTravelEvent {
    private boolean endAtSea = false;

    public TravelByCharteredBoat(Model model) {
        super(model);
    }

    @Override
    protected Sprite getSprite() {
        return TravelBySeaState.SHIP_AVATAR;
    }

    @Override
    protected boolean eventIntro(Model model) {
        return true;
    }

    @Override
    protected void eventOutro(Model model) {
        endAtSea = model.getCurrentHex() instanceof SeaHex;
        if (!endAtSea) {
            if (model.getCurrentHex().getRivers() != Direction.NONE) {
                print("You are currently in a coastal hex. Do you want to continue your journey by sea tomorrow? (Y/N) ");
                endAtSea = yesNoInput();
            }
        }
        if (!endAtSea) {
            println("You say farewell to the captain and the crew and disembark from the boat.");
        }
    }

    @Override
    protected GameState getEveningState(Model model) {
        if (!endAtSea) {
            return super.getEveningState(model);
        }
        if (checkForPirateEvent(model)) {
            DailyEventState event = new PirateShipEvent(model);
            event.doTheEvent(model);
            if (event.haveFledCombat()) {
                println("Your party escapes onto make-shift rafts and are set adrift on the surf.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToSP(-100);
                }
                while (model.getCurrentHex() instanceof SeaHex) {
                    new DriftingAtSeaState(model).run(model);
                }
                return model.getCurrentHex().getEveningState(model, false, false);
            }
        }
        return new EveningAtSeaState(model);
    }

    private boolean checkForPirateEvent(Model model) {
        if (WorldBuilder.isInExtendedRegion(model.getParty().getPosition())) {
            return MyRandom.rollD10() < 5;
        }
        return MyRandom.rollD10() == 1;
    }

    @Override
    protected String getTitleText() {
        return "TRAVEL BY SEA";
    }

    @Override
    protected String getUnderText() {
        return "You have chartered a boat and are traveling by sea.";
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a sea or coastal hex to travel to.";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 5;
    }

    @Override
    protected boolean isValidDestination(Model model, Point selectedPos) {
        return model.getWorld().getHex(selectedPos) instanceof SeaHex || model.getWorld().getHex(selectedPos).getRivers() != Direction.NONE;
    }

    @Override
    protected boolean dontAllowSeaHexes() {
        return false;
    }

    private static class DriftingAtSeaState extends RunAwayState {
        public DriftingAtSeaState(Model model) {
            super(model);
        }

        @Override
        protected Point selectDirection(Model model, MapSubView mapSubView) {
            List<Point> result = new ArrayList<>();
            for (Point dir : mapSubView.getDirections(model)) {
                if (!movesOutsideMap(model.getParty().getPosition(), dir)) {
                    result.add(dir);
                }
            }
            return MyRandom.sample(result);
        }

        @Override
        protected boolean checkForRiding(Model model) {
            println("Your party is drifting at sea. Each party member suffers 1 damage from the cold waters.");
            print("Press enter to continue.");
            waitForReturn();
            model.getParty().getHorseHandler().someHorsesRunAway(model);
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getHP() > 1) {
                    gc.addToHP(-1);
                }
            }
            return false;
        }
    }
}
