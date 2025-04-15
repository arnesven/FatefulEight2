package view.subviews;

import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class EasternPalaceSubView extends TownishSubView {
    private static final double DENSITY = 0.7;

    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x32("townhouse", "world_foreground.png", 0x43,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.WHITE),
            new Sprite32x32("townhouse2", "world_foreground.png", 0x53,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.WHITE),
            new Sprite32x32("townhouse3", "world_foreground.png", 0x73,
                    MyColors.YELLOW, PATH_COLOR, MyColors.RED, MyColors.WHITE)};

    public EasternPalaceSubView(AdvancedDailyActionState advancedDailyActionState,
                                SteppingMatrix<DailyActionNode> matrix, boolean isCoastal, String name) {
        super(advancedDailyActionState, matrix, isCoastal, name, DENSITY, HOUSE_SPRITES);
    }
}
