package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.map.CaveHex;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class FortressAtUtmostEdgeSubView extends DailyActionSubView {
    private static final Sprite32x32[][] backgroundSprites = makeFatueSprites();

    public FortressAtUtmostEdgeSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
    }

    @Override
    protected void drawBackground(Model model) {
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {
                Point p = convertToScreen(new Point(x, y));
                model.getScreenHandler().put(p.x, p.y, backgroundSprites[x][y]);
            }
        }
    }

    @Override
    protected String getPlaceType() {
        return "F.A.T.U.E.";
    }

    private static Sprite32x32[][] makeFatueSprites() {
        Sprite32x32[][] result = new Sprite32x32[8][9];
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {

                MyColors color2 = MyColors.LIGHT_GRAY;
                MyColors color3 = MyColors.DARK_RED;
                if (x == 7 && y == 8) {
                    color2 = MyColors.YELLOW;
                    color3 = CaveHex.GROUND_COLOR;
                } else if (x == 0 && y == 4) {
                    color2 = MyColors.LIGHT_GRAY;
                } else if (x == 0 || x == 7 || y == 7 || y == 8) {
                    color2 = MyColors.WHITE;
                }


                MyColors color4 = MyColors.DARK_GREEN;
                if (y == 0) {
                    color4 = MyColors.GRAY;
                } else if (y == 1 || y == 2) {
                    color4 = MyColors.GOLD;
                } else if ((y == 4 || y == 5) && x > 4) {
                    color4 = MyColors.DARK_PURPLE;
                } else if ((y == 6 || y == 7) && x < 3) {
                    color4 = MyColors.BROWN;
                } else if (x < 2) {
                    color4 = MyColors.BROWN;
                }
                result[x][y] = new FatueSprite(0x10*y + x, color2, color3, color4);
            }
        }
        return result;
    }

    private static class FatueSprite extends Sprite32x32 {
        public FatueSprite(int num, MyColors color1, MyColors color2, MyColors color3) {
            super("fatue"+num, "fatue_plan.png", num, MyColors.BLACK, color1, color2, color3);
        }
    }
}
