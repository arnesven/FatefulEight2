package model.mainstory.jungletribe;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.SubView;

import java.awt.*;

public class RubiqPuzzleSubView extends SubView {
    private static final Sprite[] BG_SPRITES = new Sprite[]{new Sprite32x32("rubiqwall", "quest.png",0xCD,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.DARK_GREEN, MyColors.BEIGE)} ;

    @Override
    protected void drawArea(Model model) {
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {
                Point p = convertToScreen(x, y);
                model.getScreenHandler().put(p.x, p.y, BG_SPRITES[0]);
            }
        }
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x * 4, Y_OFFSET + y * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "PUZZLE RUBIQ";
    }
}
