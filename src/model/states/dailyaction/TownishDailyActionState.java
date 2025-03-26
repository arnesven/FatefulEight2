package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.tavern.TavernNode;
import model.states.dailyaction.town.CampOutsideOfTownNode;
import model.states.dailyaction.town.FlagPoleNode;
import sound.BackgroundMusic;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TownishDailyActionState extends AdvancedDailyActionState {
    private final UrbanLocation urbanLocation;

    public TownishDailyActionState(Model model, boolean isCoastal, UrbanLocation urbanLocation,
                                   boolean freeLodging, boolean freeRations) {
        super(model);
        this.urbanLocation = urbanLocation;
        super.addNode(3, 4, new StayHereNode());
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y, new TavernNode(freeLodging));
        addTent(model, freeRations, urbanLocation);
        addNode(7, 2, new SaveGameNode());
        addNode(7, 1, new FlagPoleNode());
        addTravelNodes(model, isCoastal, urbanLocation);
        addShopsAndMore(model, urbanLocation);
    }

    protected void addTent(Model model, boolean freeRations, UrbanLocation urbanLocation) {
        super.addNode(0, TOWN_MATRIX_ROWS - 1, new CampOutsideOfTownNode(freeRations, model,
                TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT, "Make camp on the outskirts of town"));
    }

    protected void addShopsAndMore(Model model, UrbanLocation urbanLocation) {
        for (GeneralShopNode shop : urbanLocation.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        super.addNode(2, TOWN_MATRIX_ROWS-1, new WorkBenchNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
    }

    protected void addTravelNodes(Model model, boolean isCoastal, UrbanLocation urbanLocation) {
        super.addNode(urbanLocation.getTravelNodePosition().x, urbanLocation.getTravelNodePosition().y,
                new TravelNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return urbanLocation.makeActionSubView(model, advancedDailyActionState, matrix);
    }

    public TownLocation getTown() {
        return (TownLocation) urbanLocation;
    }

    @Override
    protected BackgroundMusic getSound() {
        return BackgroundMusic.citySong;
    }
}
