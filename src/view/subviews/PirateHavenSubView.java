package view.subviews;

import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class PirateHavenSubView extends TownishSubView {

    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                    MyColors.YELLOW, PATH_COLOR, MyColors.DARK_RED, MyColors.PEACH),
            new Sprite32x32("townhouse2", "world_foreground.png", 0x53,
                    MyColors.YELLOW, PATH_COLOR, MyColors.DARK_RED, MyColors.PEACH),
            new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                    MyColors.YELLOW, PATH_COLOR, MyColors.DARK_RED, MyColors.PEACH)};

    private static final double TOWN_DENSITY = 0.55;

    public PirateHavenSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix,
                              boolean isCoastal, String name) {
        super(state, matrix, isCoastal, name, TOWN_DENSITY, true, HOUSE_SPRITES);
    }

    @Override
    protected String getPlaceType() {
        return "PIRATE HAVEN";
    }
}
