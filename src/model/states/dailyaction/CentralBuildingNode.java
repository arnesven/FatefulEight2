package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x64;
import view.sprites.Sprite64x32;

import java.awt.*;

public class CentralBuildingNode extends DailyActionNode {
    private static final Sprite SPRITE_TOP = new Sprite64x32("centralbldgbottom", "world_foreground.png", 0xE2,
            MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.GRAY);
    private static final Sprite SPRITE_BOTTOM = new Sprite64x32("centralbldgbottom", "world_foreground.png", 0xF2,
            MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.BROWN);
    private final boolean big;

    public CentralBuildingNode(boolean big) {
        super("Central Building");
        this.big = big;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE_BOTTOM;
    }

    @Override
    public Point getCursorShift() {
        return new Point(+2, -2);
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), p, getBackgroundSprite());
        Point p2 = new Point(p);
        p2.y -= 2;
        if (big) {
            p2.y -= 2;
        }
        model.getScreenHandler().register(SPRITE_TOP.getName(), p2, SPRITE_TOP);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}
