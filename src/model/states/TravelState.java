package model.states;

import model.Model;
import model.map.Direction;
import model.map.UrbanLocation;
import model.map.World;
import model.map.WorldHex;
import model.states.events.RiverEvent;
import view.YesNoMessageView;
import view.subviews.EmptySubView;
import view.subviews.MapSubView;
import view.subviews.CollapsingTransition;

import java.awt.*;

public class TravelState extends GameState {

    public TravelState(Model model) {
        super(model);
    }

    @Override
    public final GameState run(Model model) {
        MapSubView mapSubView = new MapSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        model.getTutorial().travel(model);

        boolean riding = checkForRiding(model);

        GameState state = travelOneStep(model, mapSubView);
        if (state != null) {
            return state;
        }
        if (riding) {
            mapSubView = new MapSubView(model);
            CollapsingTransition.transition(model, mapSubView);
            state = travelOneStep(model, mapSubView);
            if (state != null) {
                return state;
            }
        }
        return nextState(model);
    }

    private boolean checkForRiding(Model model) {
        if (!model.getParty().hasHorses()) {
            return false;
        }
        if (model.getParty().canRide()) {
            print("You have enough horses for your party to ride. Do you want to? (Y/N) ");
            return yesNoInput();
        } else {
            println("Your whole party cannot ride because at least one party member does not have a suitable mount.");
        }
        return false;
    }

    private GameState travelOneStep(Model model, MapSubView mapSubView) {
        Point selectedDir = selectDirection(model, mapSubView);
        Point newPosition = new Point(model.getParty().getPosition());
        model.getWorld().move(newPosition, selectedDir.x, selectedDir.y);

        if (checkRiverCrossing(model, mapSubView)) {
            println("The party comes to a river.");
            CollapsingTransition.transition(model, RiverEvent.subView);
            RiverEvent river = model.getCurrentHex().generateRiverEvent(model);
            GameState state = river.run(model);
            if (state instanceof GameOverState) {
                return state;
            } if (river.eventPreventsCrossing(model)) {
                setCurrentTerrainSubview(model);
                return state;
            }
            CollapsingTransition.transition(model, mapSubView);
        }

        mapSubView.drawAvatarEnabled(false);
        mapSubView.addMovementAnimation(
                model.getParty().getLeader().getAvatarSprite(),
                model.getWorld().translateToScreen(model.getParty().getPosition(), model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                model.getWorld().translateToScreen(newPosition, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
        mapSubView.waitForAnimation();
        CollapsingTransition.transition(model, new EmptySubView());

        moveToHex(model, selectedDir, mapSubView);
        return null;
    }

    private void moveToHex(Model model, Point selectedDir, MapSubView mapSubView) {
        model.getCurrentHex().travelFrom(model);
        model.getParty().move(model, selectedDir.x, selectedDir.y);
        if (model.getParty().getPreviousPosition().getLocation() instanceof UrbanLocation &&
                model.getCurrentHex().hasRoad()) {
            model.getParty().setOnRoad(true);
        } else if (partyNoLongerOnRoad(model, mapSubView)) {
            model.getParty().setOnRoad(false);
        }
        setCurrentTerrainSubview(model);
        model.getCurrentHex().travelTo(model);
    }

    protected boolean partyNoLongerOnRoad(Model model, MapSubView mapSubView) {
        return model.getParty().isOnRoad() &&
                !model.getWorld().travelingAlongRoad(model.getParty().getPosition(),
                        model.getParty().getPreviousPosition(),
                        Direction.getDirectionForDxDy(model.getParty().getPreviousPosition(),
                                mapSubView.getSelectedDirection()));
    }

    protected GameState nextState(Model model) {
        return model.getCurrentHex().generateEvent(model);
    }

    protected boolean checkRiverCrossing(Model model, MapSubView mapSubView) {
        return !model.isInCaveSystem() &&
                !model.getCurrentHex().getRoadInDirection(Direction.getDirectionForDxDy(model.getParty().getPosition(), mapSubView.getSelectedDirection())) &&
                model.getWorld().crossesRiver(model.getParty().getPosition(),
                    Direction.getDirectionForDxDy(model.getParty().getPosition(), mapSubView.getSelectedDirection()));
    }

    protected Point selectDirection(Model model, MapSubView mapSubView) {
        Point selectedDir;
        do {
            print("Please select an adjacent hex to travel to: ");
            waitForReturn();
            selectedDir = mapSubView.getSelectedDirection();
        } while (selectedDir.x == 0 && selectedDir.y == 0);
        return selectedDir;
    }
}
