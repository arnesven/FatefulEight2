package view.subviews;

import model.SteppingMatrix;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class JungleVillageSubView extends TownishSubView {
    private static final double DENSITY = 0.5;

    private static final Sprite[] HOUSE_SPRITES = new Sprite[]{
            new Sprite32x32("junglehut1", "world_foreground.png", 0xAC,
                    MyColors.BLACK, PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN),
            new Sprite32x32("junglehut2", "world_foreground.png", 0xAD,
                    MyColors.BLACK, PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN),
            new Sprite32x32("junglehut3", "world_foreground.png", 0xAE,
                    MyColors.BLACK, PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN),
            new Sprite32x32("junglehut4", "world_foreground.png", 0xAF,
                    MyColors.BLACK, PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN),
            new Sprite32x32("junglehut4", "world_foreground.png", 0x9D,
                    MyColors.BLACK, PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN)
    };

    public JungleVillageSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix, String name) {
        super(state, matrix, WaterLocation.riverside, name, DENSITY, false, HOUSE_SPRITES);
    }
}
