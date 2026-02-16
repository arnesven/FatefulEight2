package view.subviews;


import model.SteppingMatrix;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.List;
import java.awt.Point;

public class TownSubView extends TownishSubView {

    public static final Sprite[] TOWN_HOUSES = new Sprite[]{
            new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                    MyColors.YELLOW, PATH_COLOR, MyColors.BROWN, MyColors.CYAN),
            new Sprite32x32("townhouse2", "world_foreground.png", 0x53,
                    MyColors.YELLOW, PATH_COLOR, MyColors.BROWN, MyColors.PINK),
            new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                    MyColors.YELLOW, PATH_COLOR, MyColors.BROWN, MyColors.WHITE)};

    private static final double TOWN_DENSITY = 0.3;

    public TownSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix,
                       WaterLocation water, String townName, List<Point> decorativeHousePositions) {
        super(state, matrix, water, townName, TOWN_DENSITY, true, TOWN_HOUSES, decorativeHousePositions);
    }
}
