package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.map.HexLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import sound.BackgroundMusic;
import view.MyColors;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.Point;

public class TownDailyActionState extends AdvancedDailyActionState {

    private final UrbanLocation urbanLocation;

    public TownDailyActionState(Model model, boolean isCoastal, UrbanLocation urbanLocation,
                                boolean freeLodging, boolean freeRations) {
        super(model);
        this.urbanLocation = urbanLocation;
        super.addNode(3, 4, new StayHereNode());
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y, new TavernNode(freeLodging));
        super.addNode(3, 3, new TownHallNode());
        addTentOrHq(model, freeRations);
        super.addNode(2, TOWN_MATRIX_ROWS-1, new WorkBenchNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
        super.addNode(4, TOWN_MATRIX_ROWS-1, new StableNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
        addNode(7, 2, new SaveGameNode());
        addNode(7, 1, new FlagPoleNode());
        addTravelNodes(model, isCoastal);
        addShopsAndMore(model);
        model.getMainStory().handleTownSetup(this);
    }

    private void addTentOrHq(Model model, boolean freeRations) {
        if (model.getParty().hasHeadquartersIn(urbanLocation)) {
            super.addNode(0, TOWN_MATRIX_ROWS - 1, new HeadquartersNode(model));
        } else {
            super.addNode(0, TOWN_MATRIX_ROWS - 1, new CampOutsideOfTownNode(freeRations, model,
                    TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT, "Make camp on the outskirts of town"));
        }
    }

    private void addShopsAndMore(Model model) {
        for (GeneralShopNode shop : urbanLocation.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        Point careerOfficePosition = urbanLocation.getCareerOfficePosition();
        if (careerOfficePosition != null) {
            addNode(careerOfficePosition.x, careerOfficePosition.y, new CareerOfficeNode());
        }
    }

    private void addTravelNodes(Model model, boolean isCoastal) {
        super.addNode(urbanLocation.getTravelNodePosition().x, urbanLocation.getTravelNodePosition().y,
                new TravelNode(model, TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
        if (isCoastal) {
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

    public TownDailyActionState(Model model, boolean isCoastal, UrbanLocation urbanLocation) {
        this(model, isCoastal, urbanLocation, false, false);
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
