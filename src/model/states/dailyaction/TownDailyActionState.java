package model.states.dailyaction;

import model.Model;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.states.dailyaction.town.*;
import view.subviews.TownSubView;

import java.awt.Point;

public class TownDailyActionState extends TownishDailyActionState {

    public TownDailyActionState(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation,
                                boolean freeLodging, boolean freeRations) {
        super(model, hasWaterAccess, urbanLocation, freeLodging, freeRations);
        addNode(7, 1, new FlagPoleNode());
        model.getMainStory().handleTownSetup(this);
        if (urbanLocation instanceof TownLocation && ((TownLocation) urbanLocation).getDecorativeHousePositions() != null) {
            for (Point p : ((TownLocation) urbanLocation).getDecorativeHousePositions()) {
                if (isPositionFilled(p.x, p.y)) {
                    throw new IllegalStateException("Tried blocking a position that was already filled! Was blocked? " + isPositionBlocked(p.x, p.y));
                } else {
                    blockPosition(p.x, p.y);
                }
            }
        }
    }

    public TownDailyActionState(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation) {
        this(model, hasWaterAccess, urbanLocation, false, false);
    }

    @Override
    protected void addTravelNodes(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation) {
        super.addTravelNodes(model, hasWaterAccess, urbanLocation);
        if (hasWaterAccess) {
            if (!urbanLocation.noBoat()) {
                if (model.getDay() % urbanLocation.charterBoatEveryNDays() == 1) {
                    addNode(1, 0, new GoTheDocksNode(model));
                    addNode(4, 0, new CharterBoatAtDocks(model));
                } else {
                    addNode(2, 0, new GoTheDocksNode(model));
                }
            }
            if (urbanLocation.bothBoatAndCarriage()) {
                addNode(TOWN_MATRIX_COLUMNS-1, 5, new GoToCarriageNode(model));
            }
        } else {
            addNode(4, 0, new GoToCarriageNode(model));
        }
    }

    @Override
    protected void addTent(Model model, boolean freeRations, UrbanLocation urbanLocation) {
        if (model.getParty().hasHeadquartersIn(urbanLocation)) {
            super.addNode(0, TOWN_MATRIX_ROWS - 1, new HeadquartersNode(model));
        } else {
            super.addTent(model, freeRations, urbanLocation);
        }
    }

    @Override
    protected void addShopsAndMore(Model model, UrbanLocation urbanLocation) {
        super.addNode(3, 3, new TownHallNode());
        blockPosition(4, 3);
        super.addShopsAndMore(model, urbanLocation);
        Point careerOfficePosition = urbanLocation.getCareerOfficePosition();
        if (careerOfficePosition != null) {
            addNode(careerOfficePosition.x, careerOfficePosition.y, new CareerOfficeNode());
        }
        super.addNode(4, TOWN_MATRIX_ROWS-1, new StableNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
    }
}
