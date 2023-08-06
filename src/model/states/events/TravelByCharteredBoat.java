package model.states.events;

import model.Model;
import model.map.Direction;
import model.map.SeaHex;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.states.GameState;
import model.states.TravelBySeaState;
import view.sprites.Sprite;

import java.awt.*;

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
                endAtSea = !yesNoInput();
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
        return new EveningAtSeaState(model);
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
        return true;
    }
}
