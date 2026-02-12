package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.AncientCityLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.AncientCityTavernNode;
import sound.BackgroundMusic;
import view.subviews.TownSubView;

import java.awt.*;

public class AncientCityDailyActionState extends TownishDailyActionState {
    public AncientCityDailyActionState(Model model, AncientCityLocation ancientCityLocation, boolean isCapital) {
        super(model, false, ancientCityLocation, false, false);
        Point central = new Point(3, 3);
        addNode(central.x, central.y, new CentralBuildingNode(isCapital));
        blockPosition(central.x + 1, central.y);
        blockPosition(central.x + 1, central.y - 1);
        blockPosition(central.x, central.y - 2);
        blockPosition(central.x + 1, central.y - 2);
    }

    @Override
    protected BackgroundMusic getSound() {
        return BackgroundMusic.caveSong;
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        blockPosition(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y-2);
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y,
                new AncientCityTavernNode(freeLodging));
    }

    @Override
    protected void addShopsAndMore(Model model, UrbanLocation urbanLocation) {
        super.addShopsAndMore(model, urbanLocation);
        super.addNode(4, TOWN_MATRIX_ROWS-1, new StableNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
    }
}
