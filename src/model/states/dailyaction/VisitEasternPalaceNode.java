package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class VisitEasternPalaceNode extends DailyActionNode {

    private static final Sprite[][] SPRITES = makePalaceSprites();

    public VisitEasternPalaceNode() {
        super("Visit Palace");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        super.drawYourself(model, p);
        for (int row = 0; row < SPRITES[0].length; ++row) {
            for (int col = 0; col < SPRITES.length; ++col) {
                if (col != 1 || row != 2) {
                    Sprite spriteToUse = SPRITES[col][row];
                    Point p2 = new Point(p.x + 4 * (col - 1), p.y + 4 * (row - 2));
                    model.getScreenHandler().register(spriteToUse.getName(), p2, spriteToUse);
                }
            }
        }
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITES[1][2];
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    private static Sprite[][] makePalaceSprites() {
        MyColors[] color1 = new MyColors[]{MyColors.WHITE, MyColors.WHITE, MyColors.WHITE, MyColors.DARK_GRAY};
        MyColors[] color3 = new MyColors[]{MyColors.RED, MyColors.RED, MyColors.BROWN, MyColors.DARK_GREEN};
        MyColors[] color2 = new MyColors[]{MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY};
        MyColors[] color4 = new MyColors[]{MyColors.DARK_RED, MyColors.DARK_RED, MyColors.DARK_RED, MyColors.TAN};
        Sprite[][] result = new Sprite32x32[3][4];
        for (int row = 0; row < result[0].length; ++row) {
            for (int col = 0; col < result.length; ++col) {
                result[col][row] = new Sprite32x32("palace" + col + "-"+ row, "world_foreground.png",
                        0x10 * row + col + 12, color1[row], color2[row], color3[row], color4[row]);
            }
        }
        return result;
    }
}
