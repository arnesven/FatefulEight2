package model.states;

import model.Model;
import model.map.World;
import model.states.events.RiverEvent;
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

        Point selectedDir = selectDirection(model, mapSubView);
        Point newPosition = new Point(model.getParty().getPosition());
        World.move(newPosition, selectedDir.x, selectedDir.y);

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
                World.translateToScreen(model.getParty().getPosition(), model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                World.translateToScreen(newPosition, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
        mapSubView.waitForAnimation();

        model.getCurrentHex().travelFrom(model);
        model.getParty().move(selectedDir.x, selectedDir.y);
        if (partyNoLongerOnRoad(model, mapSubView)) {
            model.getParty().setOnRoad(false);
        }
        setCurrentTerrainSubview(model);
        model.getCurrentHex().travelTo(model);
        return nextState(model);
    }

    protected boolean partyNoLongerOnRoad(Model model, MapSubView mapSubView) {
        return model.getParty().isOnRoad() &&
                !model.getWorld().travelingAlongRoad(model.getParty().getPosition(),
                        model.getParty().getPreviousPosition(),
                        mapSubView.getSelectedDirectionName());
    }

    protected GameState nextState(Model model) {
        return model.getCurrentHex().generateEvent(model);
    }

    protected boolean checkRiverCrossing(Model model, MapSubView mapSubView) {
        return model.getWorld().crossesRiver(model.getParty().getPosition(), mapSubView.getSelectedDirectionName());
    }

    protected Point selectDirection(Model model, MapSubView mapSubView) {
        Point selectedDir;
        do {
            print("Please select an adjacent hex to travel to: ");
            waitForReturn();
            selectedDir = mapSubView.getSelectedDirection(model);
        } while (selectedDir.x == 0 && selectedDir.y == 0);
        return selectedDir;
    }
}
